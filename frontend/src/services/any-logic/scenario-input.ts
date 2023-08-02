import {AppState} from '../appState'
import {Verblijfsobject} from '../bag-verblijfsobject'
import {Actor, defaultElectrictySupplier, defaultGovernment, defaultGridOperator} from './actor'
import {defaultEnergyAssets, EnergyAsset} from './energy-asset'
import {GridConnection} from './grid-connection'
import {defaultHsMsTransformer, defaultMsLsTransformer, GridNode} from './grid-node'
import {defaultPolicies, Policy} from './policy'

// This structure is defined by the model author
export type ScenarioInput = {
    actors: Actor[],
    gridconnections: GridConnection[],
    gridnodes: GridNode[],
    policies: Policy[],
    templateAssets: EnergyAsset[],
}

export const appStateToScenarioInput = (appState: AppState): ScenarioInput => {
    const tuples = appState.verblijfsobjecten.map(verblijfsObjectToGridConnection)

    // this is a silly way to do it
    const {actors, gridConnections} = tuples.reduce<{
        actors: Actor[],
        gridConnections: GridConnection[]
    }>(
        (acc, [actor, gridConnection]) => ({
            actors: [
                ...acc.actors,
                actor,
            ],
            gridConnections: [
                ...acc.gridConnections,
                gridConnection,
            ],
        }),
        {
            actors: [],
            gridConnections: [],
        },
    )

    return {
        actors: [
            defaultGridOperator,
            defaultGovernment,
            defaultElectrictySupplier,
            ...actors,
        ],
        gridconnections: gridConnections,
        gridnodes: [
            defaultHsMsTransformer,
            {
                ...defaultMsLsTransformer,
                capacity_kw: 1.6 * gridConnections.length,
            },
        ],
        policies: defaultPolicies,
        templateAssets: [],
    }
}
// TODO: get from netbeheerder opendata
const defaultHouseCapacityKw = (() => {
    const amps = 25
    const phases = 3
    const voltage = 230
    const toKw = 1000 ** -1

    return amps * phases * voltage * toKw
})()

const verblijfsObjectToGridConnection = (verblijfsObject: Verblijfsobject): [Actor, GridConnection] => {
    const gridConnectionId = `verblijfsObject.${verblijfsObject.identificatie}`
    const actorId = `actor.${gridConnectionId}`

    return [
        {
            id: actorId,
            category: 'CONNECTIONOWNER',
            group: 'huishoudens',
            contracts: [
                {
                    contractType: 'DELIVERY',
                    deliveryContractType: 'ELECTRICITY_FIXED',
                    contractScope: defaultElectrictySupplier.id,
                    energyCarrier: 'ELECTRICITY',
                    annualFee_eur: 20.1, // TODO: AnyLogic JSON parser gives an error if the number can be interpreted as an integer
                    deliveryPrice_eurpkWh: 0.01,
                    feedinPrice_eurpkWh: 0.01,
                },
                {
                    contractType: 'CONNECTION',
                    connectionContractType: 'DEFAULT',
                    contractScope: defaultGridOperator.id,
                    energyCarrier: 'ELECTRICITY',
                    annualFee_eur: 20.1,
                },
                {
                    contractType: 'TRANSPORT',
                    transportContractType: 'DEFAULT',
                    contractScope: defaultGridOperator.id,
                    energyCarrier: 'ELECTRICITY',
                    annualFee_eur: 20.1,
                },
                {
                    contractType: 'TAX',
                    contractScope: defaultGovernment.id,
                    energyCarrier: 'ELECTRICITY',
                    annualFee_eur: 20.1,
                    taxDelivery_eurpkWh: 0.01,
                    taxFeedin_eurpkWh: 0.01,
                    proportionalTax_pct: 0.21,
                },
            ],
        },
        {
            id: gridConnectionId,
            owner_actor: actorId,
            category: 'HOUSE',
            heating_type: 'GASBURNER',
            assets: defaultEnergyAssets,
            capacity_kw: defaultHouseCapacityKw,
            parent_electric: defaultMsLsTransformer.id,
        },
    ]
}