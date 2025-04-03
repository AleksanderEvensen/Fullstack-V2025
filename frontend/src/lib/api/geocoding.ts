import ky from 'ky'
import { z } from 'zod'

const GeocodeResultSchema = z.array(
  z.object({
    lat: z.string(),
    lon: z.string(),
    boundingbox: z.array(z.string()).length(4),
  }),
)

export async function searchGeocodeLocation(query: string) {
  try {
    const data = await ky
      .get(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json`)
      .json()
    const result = GeocodeResultSchema.safeParse(data)
    if (result.error) {
      console.error('Invalid geocode result: ', result.error)
      return undefined
    }
    return result.data
  } catch {
    return undefined
  }
}

// This helps strip away unwanted properties from the query params
const GeocodeParamsQuerySchema = z.object({
  amenity: z.string().optional(), // name and/or type of POI
  street: z.string().optional(), // 	housenumber and streetname
  city: z.string().optional(), //	city
  county: z.string().optional(), //	county
  state: z.string().optional(), //	state
  country: z.string().optional(), //	country
  postalcode: z.string().or(z.number()).optional(), //	postal code
})
type GeocodeSearchParams = z.infer<typeof GeocodeParamsQuerySchema>

/**
 * Search for a location using the structured data
 * All parameters are optional and only the ones neccessary to identify the location should be used
 *
 * @param {Object} params
 * @param {string?} params.amenity - Name and/or type of POI
 * @param {string?} params.street - Housenumber and streetname
 * @param {string?} params.city - City
 * @param {string?} params.county - County
 * @param {string?} params.state - State
 * @param {string?} params.country - Country
 * @param {string?} params.postalcode - Postal code
 */
export async function searchGeocodeAdvanced(params: GeocodeSearchParams) {
  const urlParams = new URLSearchParams()
  const parsedParamsResult = GeocodeParamsQuerySchema.safeParse(params)

  if (!parsedParamsResult.success) {
    console.error('Invalid geocode params: ', parsedParamsResult.error)
    return undefined
  }
  for (const [key, value] of Object.entries(parsedParamsResult.data)) {
    if (value) {
      urlParams.append(key, value.toString())
    }
  }

  try {
    const data = await ky
      .get(`https://nominatim.openstreetmap.org/search?${urlParams.toString()}&format=json`)
      .json()

    const result = GeocodeResultSchema.safeParse(data)
    if (result.error) {
      console.error('Invalid geocode result: ', result.error)
      return undefined
    }
    return result.data
  } catch {
    return undefined
  }
}
