import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './generic/label-row'
import {NumberRow} from './generic/number-row'
import {OldNumberInput} from './generic/old-number-input'

export const Vans = ({form, prefix}: { form: UseFormReturn, prefix: string}) => {
    const {register, watch} = form

    const numVans = watch(`${prefix}.numVans`)

    return (
        <>
            <h3>Busjes</h3>
            <NumberRow
                label="Hoeveel bestelbusjes hebben jullie in gebruik?"
                name={`${prefix}.numVans`}
                form={form} />
            {numVans > 0 && (
                <>
                    <NumberRow
                        label="Hoeveel van die bestelbusjes zijn elektrisch?"
                        name={`${prefix}.numElectricVans`}
                        form={form} />
                    <NumberRow
                        label={<span>Hoeveel <b>laadpunten</b> voor elektrische busjes hebben jullie?</span>}
                        name={`${prefix}.numChargePoints`}
                        form={form} />
                    <NumberRow
                        label="Wat is het maximale laadvermogen per laadpunt?"
                        name={`${prefix}.powerPerChargePointKw`}
                        form={form}
                        suffix="kW" />
                    <NumberRow
                        label="Hoeveel rijden jullie busjes gemiddeld per jaar (grove inschatting)?"
                        name={`${prefix}.annualTravelDistancePerVanKm`}
                        form={form}
                        suffix="km" />
                    <NumberRow
                        label="Hoeveel van de brandstof busjes zijn jullie van plan te elektrificeren de komende 5 jaar?"
                        name={`${prefix}.numPlannedElectricVans`}
                        form={form} />
                </>
            )}
        </>
    )
}