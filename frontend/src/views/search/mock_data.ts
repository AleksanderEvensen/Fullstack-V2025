function getRandomAvatar(gender: 'male' | 'female') {
  const index = Math.floor(Math.random() * 30) + 1
  if (gender === 'female') {
    return `https://avatar.iran.liara.run/public/${index}.jpg`
  }
  return `https://avatar.iran.liara.run/public/${index}.jpg`
}

type DefaultData = {
  title: string
  price: number
  city: string
  seller: {
    name: string
    avatar: string
  }
}

type Mobility = DefaultData & {
  type: 'mobility'
  year: number
  fuelType: 'diesel' | 'electric'
}

type Default = DefaultData & {
  type: 'default'
}
export type ItemCardData = Mobility | Default

export const MockSearchEntries: ItemCardData[] = [
  {
    type: 'mobility',
    title: 'Honda CB 500 Hornet',
    year: 2025,
    fuelType: 'diesel',
    city: 'Oslo',
    price: 99400, // NOK
    seller: {
      name: 'Borgenhaugen',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Oslo',
    title: 'Macbook Pro 2024',
    price: 21000, // NOK
    seller: {
      name: 'Guri Hallestad',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Tesla Model Y Long Range',
    year: 2023,
    fuelType: 'electric',
    city: 'Bergen',
    price: 490000, // NOK
    seller: {
      name: 'Erik Solberg',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Trondheim',
    title: 'Sony PlayStation 5 Pro',
    price: 7999, // NOK
    seller: {
      name: 'Marte Eriksen',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Volvo XC60 Recharge',
    year: 2024,
    fuelType: 'electric',
    city: 'Stavanger',
    price: 675000, // NOK
    seller: {
      name: 'Håkon Nilsen',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Tromsø',
    title: 'iPhone 15 Pro Max',
    price: 14990, // NOK
    seller: {
      name: 'Linda Johansen',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Triumph Street Triple RS',
    year: 2022,
    fuelType: 'diesel',
    city: 'Kristiansand',
    price: 159000, // NOK
    seller: {
      name: 'Per Andersen',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Bodø',
    title: 'Samsung QLED 65" TV',
    price: 13500, // NOK
    seller: {
      name: 'Kari Pedersen',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'BMW i4 M50',
    year: 2023,
    fuelType: 'electric',
    city: 'Ålesund',
    price: 720000, // NOK
    seller: {
      name: 'Thomas Berg',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Drammen',
    title: 'DJI Mavic 3 Pro Drone',
    price: 21990, // NOK
    seller: {
      name: 'Sofia Hansen',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Yamaha MT-09',
    year: 2024,
    fuelType: 'diesel',
    city: 'Haugesund',
    price: 145000, // NOK
    seller: {
      name: 'Ole Kristiansen',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Molde',
    title: 'Nespresso Vertuo Plus',
    price: 2490, // NOK
    seller: {
      name: 'Ingrid Larsen',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Audi e-tron GT',
    year: 2023,
    fuelType: 'electric',
    city: 'Sandefjord',
    price: 1150000, // NOK
    seller: {
      name: 'Anders Olsen',
      avatar: getRandomAvatar('male'),
    },
  },
  {
    type: 'default',
    city: 'Fredrikstad',
    title: 'Canon EOS R6 Mark II',
    price: 29990, // NOK
    seller: {
      name: 'Emma Dahl',
      avatar: getRandomAvatar('female'),
    },
  },
  {
    type: 'mobility',
    title: 'Kawasaki Ninja ZX-10R',
    year: 2025,
    fuelType: 'diesel',
    city: 'Halden',
    price: 219000, // NOK
    seller: {
      name: 'Martin Bakke',
      avatar: getRandomAvatar('male'),
    },
  },
]
