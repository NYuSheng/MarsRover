import {render, screen} from "@testing-library/react";
import React from "react";
import Header from "../../components/Header";

describe('Header component', () => {

    it('should render Header', function () {
        render(<Header/>);
        const headerElement = screen.getByText("Mars Rover Console");
        expect(headerElement).toBeInTheDocument();
    });

})
