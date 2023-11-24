import {css} from '@emotion/react'
import {FunctionComponent, PropsWithChildren, ReactElement, ReactNode} from 'react'

export const LabelRow = ({label, children}: PropsWithChildren<{label: any}>) => {
    let labelElement: ReactElement | undefined
    if (typeof label === "string") {
        labelElement = <div>{label}</div>
    } else {
        labelElement = label
    }

    return (
        <label css={css`
            display: flex;
          
            & > *:first-child {
                text-align: right;
                width: 50%;
                padding: 0.3rem;
            }
            & > *:nth-child(2) {
                width: 50%;
                padding: 0.3rem;
            }
        `}>
            {labelElement}
            <div>{children}</div>
        </label>
    )
}