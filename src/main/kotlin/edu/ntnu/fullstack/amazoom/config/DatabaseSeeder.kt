package edu.ntnu.fullstack.amazoom.config

import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.chat.repository.ChatMessageRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import jakarta.persistence.Tuple
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.xml.stream.Location

@Component
@DependsOn("flywayInitializer")
@Profile("!test")
class DatabaseSeeder @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val listingRepository: ListingRepository,
    private val listingBookmarkRepository: ListingBookmarkRepository,
    private val passwordEncoder: PasswordEncoder,
    private val chatMessageRepository: ChatMessageRepository
) : ApplicationRunner {

    private val logger = LoggerFactory.getLogger(DatabaseSeeder::class.java)
    
    @Transactional
    override fun run(args: ApplicationArguments) {
        logger.info("Checking if database schema is ready for seeding...")
        
        // Check if roles exist (which should be created by Flyway migrations)
        val roleCount = roleRepository.count()
        if (roleCount == 0L) {
            logger.warn("No roles found in database. Flyway migrations may not have completed yet. Skipping seeding.")
            return
        }
        
        logger.info("Database schema is ready. Starting database seeding...")
        val categories = createCategories()
        val users = createUsers()
        val adminListing = createAdminListing(users[0], categories[0])
        val listings = createListings(users, categories)
        createBookmarks(users, listings)
        createConversation(users[0], users[1], adminListing)

        logger.info("Database seeding completed!")
    }

    private fun createAdminListing(adminUser: User, category: Category): Listing {
        if (listingRepository.count() > 0) {
            val adminListings = listingRepository.findAll().filter { it.seller.id == adminUser.id }
            if (adminListings.isNotEmpty()) {
                return adminListings.first()
            }
            return listingRepository.findAll().first()
        }

        logger.info("Creating admin listing...")
        val adminListing = Listing(
            title = "MacBook Pro M1 Max 16-inch",
            category = category,
            condition = ListingCondition.VERY_GOOD,
            seller = adminUser,
            price = 15000.0,
            originalPrice = 25000.0,
            description = "This is an excellent condition MacBook Pro with M1 Max chip, 32GB RAM, and 1TB SSD. " +
                    "Used for about 1 year for software development. Comes with original box and charger.",
            modelYear = 2022,
            manufacturer = "Apple",
            model = "MacBook Pro 16-inch",
            serialNumber = "C02G7RZRMD6M",
            purchaseDate = "2022-05-15",
            usageDuration = "12 months",
            defects = listOf("Minor scratch on bottom case"),
            modifications = listOf("Applied screen protector"),
            reasonForSelling = "Upgrading to newer model",
            images = listOf("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?q=80&w=1000&auto=format&fit=crop")
        )
        return listingRepository.save(adminListing)
    }

    private fun createConversation(seller: User, buyer: User, listing: Listing) {
        val existingMessages = chatMessageRepository.findMessagesBetweenUsersForListing(
            seller.id, buyer.id, listing.id
        )

        if (!existingMessages.isEmpty()) {
            logger.info("Chat messages already exist between users. Skipping conversation creation.")
            return
        }

        logger.info("Creating conversation between x    s...")

        val conversation = listOf(
            ChatMessage(
                sender = buyer,
                recipient = seller,
                listing = listing,
                content = "Hi there! Is this MacBook Pro still available?",
                timestamp = Instant.now().minus(5, ChronoUnit.DAYS)
            ),
            ChatMessage(
                sender = seller,
                recipient = buyer,
                listing = listing,
                content = "Yes, it's still available! Are you interested?",
                timestamp = Instant.now().minus(5, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
            ),
            ChatMessage(
                sender = buyer,
                recipient = seller,
                listing = listing,
                content = "Definitely! Does it have any issues I should know about?",
                timestamp = Instant.now().minus(4, ChronoUnit.DAYS)
            ),
            ChatMessage(
                sender = seller,
                recipient = buyer,
                listing = listing,
                content = "No major issues. There's a small scratch on the bottom case as mentioned in the listing, but it doesn't affect functionality at all.",
                timestamp = Instant.now().minus(4, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS)
            ),
            ChatMessage(
                sender = buyer,
                recipient = seller,
                listing = listing,
                content = "That's fine. What about the battery health?",
                timestamp = Instant.now().minus(3, ChronoUnit.DAYS)
            ),
            ChatMessage(
                sender = seller,
                recipient = buyer,
                listing = listing,
                content = "Battery health is at 92%, which is excellent for a year-old laptop. I've mainly used it connected to power.",
                timestamp = Instant.now().minus(3, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS)
            ),
            ChatMessage(
                sender = buyer,
                recipient = seller,
                listing = listing,
                content = "Great! Would you consider 14,000 for it?",
                timestamp = Instant.now().minus(2, ChronoUnit.DAYS)
            ),
            ChatMessage(
                sender = seller,
                recipient = buyer,
                listing = listing,
                content = "I could go down to 14,500. It's really in excellent condition and comes with the original packaging.",
                timestamp = Instant.now().minus(2, ChronoUnit.DAYS).plus(4, ChronoUnit.HOURS)
            ),
            ChatMessage(
                sender = buyer,
                recipient = seller,
                listing = listing,
                content = "Deal! When can I pick it up?",
                timestamp = Instant.now().minus(1, ChronoUnit.DAYS)
            ),
            ChatMessage(
                sender = seller,
                recipient = buyer,
                listing = listing,
                content = "Great! How about tomorrow afternoon at the university campus?",
                timestamp = Instant.now().minus(12, ChronoUnit.HOURS)
            )
        )

        chatMessageRepository.saveAll(conversation)
        logger.info("Created conversation with ${conversation.size} messages")
    }
    
    private fun createCategories(): List<Category> {
        if (categoryRepository.count() == 0L) {
            logger.info("Creating categories...")
            val categories = listOf(
                Category(
                    name = "Electronics",
                    description = "Electronic devices and equipment",
                    translationString = "categories.electronics",
                    icon = "Laptop",
                ),
                Category(
                    name = "Furniture",
                    description = "Home and office furniture",
                    translationString = "categories.furniture",
                    icon = "Armchair"
                ),
                Category(
                    name = "Clothing",
                    description = "Apparel and accessories",
                    translationString = "categories.clothing",
                    icon = "ShirtIcon"
                ),
                Category(
                    name = "Books",
                    description = "Books, textbooks, and literature",
                    translationString = "categories.books",
                    icon = "BookOpen"
                ),
                Category(
                    name = "Sports",
                    description = "Sports equipment and gear",
                    translationString = "categories.sports",
                    icon = "Dumbbell"
                ),
                Category(
                    name = "Vehicles",
                    description = "Cars, bikes, and other vehicles",
                    translationString = "categories.vehicles",
                    icon = "Car"
                ),
                Category(
                    name = "Home",
                    description = "Home appliances and decor",
                    translationString = "categories.home",
                    icon = "HomeIcon"
                ),
                Category(
                    name = "Gaming",
                    description = "Video games and gaming equipment",
                    translationString = "categories.gaming",
                    icon = "Gamepad2"
                ),
                Category(
                    name = "Music",
                    description = "Musical instruments and equipment",
                    translationString = "categories.music",
                    icon = "Music"
                ),
                Category(
                    name = "Photography",
                    description = "Cameras and photography equipment",
                    translationString = "categories.photography",
                    icon = "Camera"
                ),
                Category(
                    name = "Garden",
                    description = "Gardening tools and outdoor items",
                    translationString = "categories.garden",
                    icon = "Flower2"
                ),
                Category(
                    name = "Tools",
                    description = "Tools and DIY equipment",
                    translationString = "categories.tools",
                    icon = "Wrench"
                )
            )
            return categoryRepository.saveAll(categories)
        }
        return categoryRepository.findAll()
    }
    
    private fun createUsers(): List<User> {
        if (userRepository.count() == 0L) {
            logger.info("Creating users...")
            val userRole = roleRepository.findByName(RoleName.ROLE_USER)
                ?: throw IllegalStateException("User role not found")
            val adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                ?: throw IllegalStateException("Admin role not found")
            
            // Create a few admin users
            val adminUsers = listOf(
                User(
                    name = "Admin Admin",
                    email = "admin@example.com",
                    password = passwordEncoder.encode("admin123"),
                    phoneNumber = "34567890",
                    address = createAddress("Karl Johans gate", "1", "0154", "Oslo", "Norway"),
                    roles = mutableSetOf(userRole, adminRole)
                ),
                User(
                    name = "Super Admin",
                    email = "super.admin@example.com",
                    password = passwordEncoder.encode("admin123"),
                    phoneNumber = "34567891",
                    address = createAddress("Storgata", "7", "0155", "Oslo", "Norway"),
                    roles = mutableSetOf(userRole, adminRole)
                )
            )
            
            // Create regular users
            val norwegianCities = listOf(
                "Oslo", "Bergen", "Trondheim", "Stavanger", "Drammen", 
                "Fredrikstad", "Kristiansand", "Sandnes", "Tromsø", "Sarpsborg",
                "Skien", "Ålesund", "Sandefjord", "Haugesund", "Tønsberg",
                "Moss", "Porsgrunn", "Bodø", "Arendal", "Hamar"
            )
            
            val firstNames = listOf(
                "Ola", "Kari", "Per", "Anne", "Jan", "Liv", "Erik", "Hilde", "Lars", "Marit",
                "Bjørn", "Eva", "Geir", "Nina", "Nils", "Ingrid", "Hans", "Tone", "Johan", "Silje",
                "Thomas", "Maria", "Morten", "Anna", "Terje", "Kristin", "Anders", "Ida", "Martin", "Hanna"
            )
            
            val lastNames = listOf(
                "Hansen", "Johansen", "Olsen", "Larsen", "Andersen", "Pedersen", "Nilsen", "Kristiansen", "Jensen", "Karlsen",
                "Johnsen", "Pettersen", "Eriksen", "Berg", "Haugen", "Hagen", "Johannessen", "Andreassen", "Jacobsen", "Dahl",
                "Jørgensen", "Halvorsen", "Henriksen", "Lund", "Sørensen", "Jakobsen", "Gundersen", "Moen", "Iversen", "Strand"
            )
            
            val regularUsers = (1..48).map { index ->
                val firstName = firstNames.random()
                val lastName = lastNames.random()
                val city = norwegianCities.random()
                val streetNumber = (1..100).random().toString()
                val postalCode = String.format("%04d", (1000..9999).random())
                
                User(
                    name = "$firstName $lastName",
                    email = "${firstName.lowercase()}.${lastName.lowercase()}${index}@example.com",
                    password = passwordEncoder.encode("password"),
                    phoneNumber = "${(10000000..99999999).random()}",
                    address = createAddress(
                        "Gateveien",
                        streetNumber,
                        postalCode,
                        city,
                        "Norway"
                    ),
                    roles = mutableSetOf(userRole)
                )
            }
            
            val allUsers = adminUsers + regularUsers
            return userRepository.saveAll(allUsers)
        }
        return userRepository.findAll()
    }
    
    private fun createAddress(
        streetName: String,
        streetNumber: String,
        postalCode: String,
        city: String,
        country: String
    ): Address {
        return Address(
            streetName = streetName,
            streetNumber = streetNumber,
            postalCode = postalCode,
            city = city,
            country = country
        )
    }
    
    private fun createListings(users: List<User>, categories: List<Category>): List<Listing> {
        if (listingRepository.count() == 0L) {
            logger.info("Creating listings...")
            
            // Product data for various categories
            val electronicsProducts = listOf(
                Triple("MacBook Pro 2022", "Apple", 12000.0),
                Triple("iPhone 14 Pro", "Apple", 8500.0),
                Triple("Samsung Galaxy S23", "Samsung", 7500.0),
                Triple("iPad Air", "Apple", 5000.0),
                Triple("Sony Bravia TV", "Sony", 9000.0),
                Triple("Bose QuietComfort Headphones", "Bose", 2500.0),
                Triple("Canon EOS R5 Camera", "Canon", 15000.0),
                Triple("Dell XPS 15", "Dell", 11000.0),
                Triple("Nintendo Switch", "Nintendo", 3000.0),
                Triple("PlayStation 5", "Sony", 5000.0)
            )
            
            val furnitureProducts = listOf(
                Triple("Ergonomic Office Chair", "Herman Miller", 5000.0),
                Triple("IKEA MALM Desk", "IKEA", 800.0),
                Triple("Leather Sofa", "BoConcept", 15000.0),
                Triple("Queen Size Bed", "Tempur", 12000.0),
                Triple("Dining Table Set", "IKEA", 3500.0),
                Triple("Bookshelf", "String", 4000.0),
                Triple("Coffee Table", "IKEA", 600.0),
                Triple("Wardrobe", "IKEA", 2800.0),
                Triple("Nightstand", "IKEA", 400.0),
                Triple("Floor Lamp", "Flos", 2000.0)
            )
            
            val clothingProducts = listOf(
                Triple("Winter Jacket", "Patagonia", 2000.0),
                Triple("Jeans", "Levi's", 800.0),
                Triple("Running Shoes", "Nike", 1200.0),
                Triple("Dress Shirt", "Ralph Lauren", 700.0),
                Triple("Summer Dress", "H&M", 400.0),
                Triple("Leather Boots", "Dr. Martens", 1800.0),
                Triple("Wool Sweater", "Devold", 1200.0),
                Triple("Backpack", "Fjällräven", 900.0),
                Triple("Sunglasses", "Ray-Ban", 1500.0),
                Triple("Watch", "Seiko", 2500.0)
            )
            
            val bookProducts = listOf(
                Triple("Harry Potter Collection", "Bloomsbury", 800.0),
                Triple("Lord of the Rings Trilogy", "HarperCollins", 600.0),
                Triple("The Girl with the Dragon Tattoo", "Norstedts Förlag", 200.0),
                Triple("A Song of Ice and Fire Set", "Bantam Books", 700.0),
                Triple("The Hunger Games Trilogy", "Scholastic", 450.0),
                Triple("To Kill a Mockingbird", "Harper Lee", 150.0),
                Triple("1984", "George Orwell", 120.0),
                Triple("Pride and Prejudice", "Jane Austen", 100.0),
                Triple("The Great Gatsby", "F. Scott Fitzgerald", 120.0),
                Triple("Web Development Textbook", "O'Reilly", 350.0)
            )
            
            val sportsProducts = listOf(
                Triple("Mountain Bike", "Scott", 8000.0),
                Triple("Tennis Racket", "Wilson", 1200.0),
                Triple("Yoga Mat", "Manduka", 800.0),
                Triple("Dumbbells Set", "Reebok", 1500.0),
                Triple("Football", "Adidas", 250.0),
                Triple("Ski Equipment", "Atomic", 4500.0),
                Triple("Basketball", "Spalding", 200.0),
                Triple("Golf Clubs Set", "Callaway", 7000.0),
                Triple("Hiking Boots", "Salomon", 1800.0),
                Triple("Snowboard", "Burton", 3500.0)
            )
            
            val vehicleProducts = listOf(
                Triple("Mountain Bike", "Scott", 12000.0),
                Triple("Electric Scooter", "Xiaomi", 4000.0),
                Triple("Used Volvo XC60", "Volvo", 350000.0),
                Triple("City Bike", "Trek", 5000.0),
                Triple("Electric Skateboard", "Boosted", 6000.0),
                Triple("Kayak", "Perception", 7000.0),
                Triple("Winter Tires Set", "Nokian", 8000.0),
                Triple("Car Roof Box", "Thule", 4500.0),
                Triple("Bike Helmet", "Giro", 1200.0),
                Triple("Car Stereo System", "Pioneer", 3000.0)
            )
            
            // Other categories
            val homeProducts = listOf(
                Triple("Vacuum Cleaner", "Dyson", 4000.0),
                Triple("Coffee Machine", "Moccamaster", 2500.0),
                Triple("Microwave Oven", "Samsung", 1500.0),
                Triple("Blender", "KitchenAid", 2000.0),
                Triple("Toaster", "Smeg", 1200.0),
                Triple("Bed Linens", "IKEA", 400.0),
                Triple("Air Purifier", "Philips", 2500.0),
                Triple("Cooking Pots Set", "Tefal", 1000.0),
                Triple("Kitchen Knives Set", "Global", 2000.0),
                Triple("Indoor Plants", "Various", 300.0)
            )
            
            // Create a random selection of conditions
            val conditions = ListingCondition.values()
            
            // Generic descriptions
            val descriptions = listOf(
                "In excellent condition. Barely used.",
                "Good condition with minor wear and tear.",
                "Works perfectly. Selling because I'm upgrading.",
                "Like new condition. No scratches or defects.",
                "Still in great shape. Selling due to moving abroad.",
                "Some signs of use but functions perfectly.",
                "Purchased last year, still under warranty.",
                "Great value for the price.",
                "Regularly maintained and well cared for.",
                "Comes with original packaging and accessories."
            )
            
            // Build the listings
            val allListings = mutableListOf<Listing>()
            val listingCount = 100
            val usersForListings = users.filter { !it.roles.any { role -> role.name == RoleName.ROLE_ADMIN } }
            
            // Make sure we find all categories needed
            val categoryMap = categories.associateBy { it.name }
            val electronics = categoryMap["Electronics"] ?: throw IllegalStateException("Electronics category not found")
            val furniture = categoryMap["Furniture"] ?: throw IllegalStateException("Furniture category not found")
            val clothing = categoryMap["Clothing"] ?: throw IllegalStateException("Clothing category not found")
            val books = categoryMap["Books"] ?: throw IllegalStateException("Books category not found")
            val sports = categoryMap["Sports"] ?: throw IllegalStateException("Sports category not found")
            val vehicles = categoryMap["Vehicles"] ?: throw IllegalStateException("Vehicles category not found")
            val home = categoryMap["Home"] ?: throw IllegalStateException("Home category not found")
            val gaming = categoryMap["Gaming"] ?: categoryMap["Electronics"] ?: throw IllegalStateException("Gaming or Electronics category not found")
            val music = categoryMap["Music"] ?: categoryMap["Home"] ?: throw IllegalStateException("Music or Home category not found")
            val photography = categoryMap["Photography"] ?: categoryMap["Electronics"] ?: throw IllegalStateException("Photography or Electronics category not found")
            
            // Generic stock images
            val productImages = listOf(
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1581235720704-06d3acfcb36f?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1583394838336-acd977736f90?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1507035895480-2b3156c31fc8?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1583934584597-5d9e9c609a7e?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1544816155-12df9643f363?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1617005082133-45a4e4376e3d?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?q=80&w=1000&auto=format&fit=crop"
            )
            
            // Create a variety of listings for different categories
            val createdListings = mutableListOf<Listing>()
            
            // Add all the seed products first
            electronicsProducts.forEach {
                createdListings.add(createListing(it, electronics, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            furnitureProducts.forEach {
                createdListings.add(createListing(it, furniture, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            clothingProducts.forEach {
                createdListings.add(createListing(it, clothing, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            bookProducts.forEach {
                createdListings.add(createListing(it, books, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            sportsProducts.forEach {
                createdListings.add(createListing(it, sports, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            vehicleProducts.forEach {
                createdListings.add(createListing(it, vehicles, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            homeProducts.forEach {
                createdListings.add(createListing(it, home, usersForListings.random(), conditions.random(), descriptions.random(), productImages.random()))
            }
            
            // Add more random listings to reach desired count
            val remainingCount = listingCount - createdListings.size
            val allProducts = listOf(
                electronicsProducts, furnitureProducts, clothingProducts, bookProducts,
                sportsProducts, vehicleProducts, homeProducts
            ).flatten()
            
            val allCategories = listOf(
                electronics, furniture, clothing, books, sports, vehicles, home,
                gaming, music, photography
            )
            
            for (i in 1..remainingCount) {
                val productInfo = allProducts.random()
                val category = allCategories.random()
                val seller = usersForListings.random()
                val condition = conditions.random()
                val description = descriptions.random()
                val image = productImages.random()
                
                createdListings.add(createListing(productInfo, category, seller, condition, description, image))
            }
            
            return listingRepository.saveAll(createdListings)
        }
        return listingRepository.findAll()
    }
    
    private fun createListing(
        productInfo: Triple<String, String, Double>,
        category: Category,
        seller: User,
        condition: ListingCondition,
        description: String,
        imageUrl: String
    ): Listing {
        val (title, manufacturer, price) = productInfo
        val originalPrice = if (condition != ListingCondition.NEW) price * (1.2 + (0..5).random() * 0.1) else null
        val (latitude, longitude) = randomLatLon();
        return Listing(
            title = title,
            category = category,
            condition = condition,
            seller = seller,
            price = price,
            originalPrice = originalPrice,
            description = "$title: $description",
            manufacturer = manufacturer,
            latitude = latitude,
            longitude = longitude,
            modelYear = if ((0..1).random() == 1) (2019..2025).random() else null,
            purchaseDate = if ((0..1).random() == 1) "${(2019..2023).random()}-${(1..12).random().toString().padStart(2, '0')}-${(1..28).random().toString().padStart(2, '0')}" else null,
            usageDuration = if ((0..1).random() == 1) "${(1..36).random()} months" else null,
            defects = if ((0..3).random() == 0) listOf("Minor scratches", "Small dent") else emptyList(),
            modifications = if ((0..5).random() == 0) listOf("Upgraded RAM", "New battery") else emptyList(),
            reasonForSelling = if ((0..1).random() == 1) "Upgrading to a newer model" else null,
            images = listOf(imageUrl)
        )
    }
    
    private fun createBookmarks(users: List<User>, listings: List<Listing>) {
        if (listingBookmarkRepository.count() == 0L && listings.isNotEmpty() && users.size >= 2) {
            logger.info("Creating bookmarks...")
            
            val bookmarks = mutableListOf<ListingBookmark>()
            val regularUsers = users.filter { !it.roles.any { role -> role.name == RoleName.ROLE_ADMIN } }
            
            // Each user bookmarks 5-15 random listings that they don't own
            regularUsers.forEach { user ->
                val userListings = listings.filter { it.seller.id != user.id }
                val bookmarkCount = (5..15).random()
                val userBookmarks = userListings.shuffled().take(bookmarkCount)
                
                userBookmarks.forEach { listing ->
                    bookmarks.add(
                        ListingBookmark(
                            listing = listing,
                            user = user
                        )
                    )
                }
            }
            
            listingBookmarkRepository.saveAll(bookmarks)
            logger.info("Created ${bookmarks.size} bookmarks")
        }
    }

    private fun randomLatLon(): Pair<Double, Double> {
        val locations = mapOf(
            "trondheim" to LocationBounds(
                63.381237, 10.339532,
                63.430249, 10.478325
            ),
            "oslo" to LocationBounds(
                59.916931, 10.573178,
                59.958125, 10.860918
            )
        )
        // get random map location map key
        val randomLocationName = locations.keys.random()
        val location = locations[randomLocationName]!!;

        val lat = Random().nextDouble(location.minLat, location.maxLat);
        val lon = Random().nextDouble(location.minLon, location.maxLon);
        return Pair(lat, lon);

    }
}

data class LocationBounds(
    val minLat: Double,
    val minLon: Double,
    val maxLat: Double,
    val maxLon: Double
)