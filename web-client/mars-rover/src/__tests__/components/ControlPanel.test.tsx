import React from 'react';
import {render, screen} from "@testing-library/react";
import ControlPanel from "../../components/ControlPanel";

describe('ControlPanel Component', () => {

    it('should render create and move components', function () {
        render(<ControlPanel rovers={[]} moveSelectedRover={jest.fn()} addRover={jest.fn()}/>);

        const createElement = screen.getByText("Create rover");
        const moveElement = screen.getByText("Move rover");

        expect(createElement).toBeInTheDocument();
        expect(moveElement).toBeInTheDocument();
    });

})