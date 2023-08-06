import React from "react";
import {Rover} from "../../model/Rover";
import {Direction} from "../../model/Direction";
import {render} from "@testing-library/react";
import Map from "../../components/Map";

describe('Map Component', () => {

    it('should be able to render rovers passed in from props', function () {
        const rovers: Rover[] = [
            {id: "R1", coordinate: {x: 1, y: 1, direction: Direction.E}},
            {id: "R5", coordinate: {x: 5, y: 2, direction: Direction.N}},
        ]

        const {container} = render(<Map rovers={rovers}/>);
        const loadedRovers = container.getElementsByClassName("recharts-scatter-symbol")
        expect(loadedRovers.length).toEqual(2)
    });

})