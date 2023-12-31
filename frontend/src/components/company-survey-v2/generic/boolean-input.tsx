import {Radio} from 'antd'
import {Controller, UseFormReturn} from 'react-hook-form'

export const BooleanInput = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    <Radio value={true}>Ja</Radio>
                    <Radio value={false}>Nee</Radio>
                </Radio.Group>
            )}
        />
    )
}