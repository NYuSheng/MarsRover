import React from "react";
import {render} from "@testing-library/react";
import {RoverTooltip} from "../../components/RoverTooltip";

describe('RoverTooltip Component', () => {

    it('should display tooltip with rover details if available', function () {
        const payload = [
            {payload: {id: "R1", direction: "N", x: 3, y: 4}, value: 3},
            {payload: {id: "R1", direction: "N", x: 3, y: 4}, value: 4}
        ]

        const {getByText} = render(<RoverTooltip active={true} payload={payload}/>)

        expect(getByText('Rover ID : R1')).toBeInTheDocument()
        expect(getByText('X : 3')).toBeInTheDocument()
        expect(getByText('Y : 4')).toBeInTheDocument()
        expect(getByText('Direction : North')).toBeInTheDocument()
    });

    it('should not display anything if not active and payload is not available', function () {
        const {queryByText} = render(<RoverTooltip active={false} payload={null}/>)

        expect(queryByText('Rover ID')).not.toBeInTheDocument()
        expect(queryByText('X')).not.toBeInTheDocument()
        expect(queryByText('Y')).not.toBeInTheDocument()
        expect(queryByText('Direction')).not.toBeInTheDocument()
    });

})