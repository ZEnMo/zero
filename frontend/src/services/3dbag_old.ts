import {BBox2d} from '@turf/helpers/dist/js/lib/geojson'
import {Polygon} from 'geojson'
import {LatLngBounds} from 'leaflet'

type ResponseBody = {
    type: 'FeatureCollection',
    features: Bag3DFeature[],
    totalFeatures: number, // not part of the WFS standard
    numberMatched: number,
    numberReturned: number,
    timestamp: string,
    crs: {
        type: 'name',
        properties: {
            name: 'urn:ogc:def:crs:EPSG::4326',
        }
    }
    bbox: BBox2d,
}

export type Bag3DFeature = {
    type: 'Feature',
    id: string, // example: lod12.7552479
    geometry: Polygon,
    geometry_name: 'geometrie',
    properties: Bag3dProperties
}

export type Bag3dProperties = {
    gid: number
    fid: number
    h_dak_min: number
    h_dak_50p: number
    h_dak_70p: number
    h_dak_max: number
    identificatie: string // example: "NL.IMBAG.Pand.0772100000304368"
    oorspronkelijk_bouwjaar: number
    status: string // e.g. "Pand in gebruik"
    geconstateerd: boolean
    documentdatum: string // example "2001-07-04Z"
    documentnummer: string // example "2000/1411"
    voorkomenidentificatie: number
    begingeldigheid: string // example "2001-07-04Z"
    eindgeldigheid: string // example "2001-07-04Z"
    tijdstipinactief: null
    tijdstipregistratielv: null
    tijdstipeindregistratielv: null
    tijdstipinactieflv: null
    tijdstipnietbaglv: null
    h_maaiveld: number // needed to determine building height
    dak_type: string // example "horizontal"
    pw_datum: string //example "2016-12-01Z"
    pw_actueel: string //example "yes"
    pw_bron: string //example "ahn3"
    reconstructie_methode: string //example "tudelft3d-geoflow"
    versie_methode: string // example "v21.09.08"
    kas_warenhuis: boolean
    ondergronds_type: string // example "above ground"
    q_t_run: number
    q_data_area: number
    q_nodata_area: number
    q_data_coverage: number
    kwaliteits_klasse_lod13_2d: null
}

export async function fetchBag3dPanden(boundingBox: LatLngBounds, startIndex = 0): Promise<Bag3DFeature[]> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'BAG3D_v2:lod12',
        srsName: 'EPSG:4326', // output coordinate system
        outputFormat: 'json',
        bbox: [
            boundingBox.getWest(),
            boundingBox.getSouth(),
            boundingBox.getEast(),
            boundingBox.getNorth(),
            'EPSG:4326', // input coordinate system
        ].join(','),
        startIndex: startIndex.toString(),
        // TODO: check if we can limit to only relevant properties using parameter "PropertyName"
    })

    // PDOK says we shouldn't use this endpoint.
    // But the official new endpoint doesn't have the properties we want.
    // See https://geoforum.nl/t/missing-properties-in-getfeatures/8482
    const url = 'https://data.3dbag.nl/api/BAG3D_v2/wfs?' + params.toString()

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting BAG 3D data')
    }

    const body = await response.json() as ResponseBody
    if (body.numberReturned + startIndex >= body.numberMatched) {
        // this is the last page
        return body.features
    }

    // recurse to get the next page
    return [
        ...body.features,
        ...await fetchBag3dPanden(boundingBox, startIndex + body.numberReturned)
    ]
}
