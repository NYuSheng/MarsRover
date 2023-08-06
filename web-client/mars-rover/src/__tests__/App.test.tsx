import React from 'react';
import {render, screen} from '@testing-library/react';
import App from '../App';

describe('App Component', () => {

    it('should render header, create and move components', function () {
        render(<App/>);

        const headerElement = screen.getByText("Mars Rover Console");
        const createElement = screen.getByText("Create rover");
        const moveElement = screen.getByText("Move rover");

        expect(headerElement).toBeInTheDocument();
        expect(createElement).toBeInTheDocument();
        expect(moveElement).toBeInTheDocument();
    });

})