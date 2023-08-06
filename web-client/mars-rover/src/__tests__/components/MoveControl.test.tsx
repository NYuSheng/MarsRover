import React from 'react';
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import {Rover} from "../../model/Rover";
import {Direction} from "../../model/Direction";
import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import MoveControl from "../../components/MoveControl";
import {rootUrl} from "../../http/RoverRequests";

describe('MoveControl Component', () => {

    const moveRover = jest.fn()
    const rovers: Rover[] = [
        {id: "R1", coordinate: {x: 1, y: 1, direction: Direction.E}},
        {id: "R5", coordinate: {x: 5, y: 2, direction: Direction.N}},
    ]

    let mock: MockAdapter;

    beforeAll(() => {
        mock = new MockAdapter(axios)
    })

    afterEach(() => {
        mock.reset()
    })

    it('should have default inputs upon load', function () {
        render(<MoveControl rovers={rovers} moveSelectedRover={moveRover}/>)

        const roverSelect = screen.getByLabelText('rover-select')
        const commandInput = screen.getByLabelText('command-input')

        expect(roverSelect.value).toBe('')
        expect(commandInput.value).toBe('')
    });

    it('should be able to move rover based on the given inputs', async function () {
        jest.spyOn(window, 'alert').mockImplementation(() => {
        });
        const rover: Rover = {id: "R1", coordinate: {x: 5, y: 9, direction: Direction.E}}
        const expectedUrl = `${rootUrl}/api/v1/moveRover?roverId=R1&moveCommands=f,b,r,l`
        mock.onPut(expectedUrl).reply(200, rover)

        render(<MoveControl rovers={rovers} moveSelectedRover={moveRover}/>)

        const roverSelect = screen.getByLabelText('rover-select')
        const frontBtn = screen.getByText('Front')
        const backBtn = screen.getByText('Back')
        const rightBtn = screen.getByText('Right')
        const leftBtn = screen.getByText('Left')
        const moveBtn = screen.getByText('Move')

        fireEvent.change(roverSelect, {target: {value: "R1"}})
        fireEvent.click(frontBtn)
        fireEvent.click(backBtn)
        fireEvent.click(rightBtn)
        fireEvent.click(leftBtn)
        fireEvent.click(moveBtn)

        await waitFor(() => {
            expect(mock.history.put[0].url).toEqual(expectedUrl)
            expect(moveRover).toBeCalledWith(rover)
        })
    });

    it('should be able to clear command inputs', function () {
        render(<MoveControl rovers={rovers} moveSelectedRover={moveRover}/>)

        const frontBtn = screen.getByText('Front')
        const backBtn = screen.getByText('Back')
        const rightBtn = screen.getByText('Right')
        const leftBtn = screen.getByText('Left')
        const clearBtn = screen.getByText('Clear')
        const commandInput = screen.getByLabelText('command-input')

        fireEvent.click(frontBtn)
        fireEvent.click(backBtn)
        fireEvent.click(rightBtn)
        fireEvent.click(leftBtn)

        expect(commandInput.value).toBe('f,b,r,l')

        fireEvent.click(clearBtn)

        expect(commandInput.value).toBe('')
    });

    it('should disable move button if no rover is selected', function () {
        render(<MoveControl rovers={rovers} moveSelectedRover={moveRover}/>)

        const roverSelect = screen.getByLabelText('rover-select')
        const moveBtn = screen.getByText('Move')

        expect(roverSelect.value).toBe('')
        expect(moveBtn.disabled).toBeTruthy()
    });

    it('should disable move button if no command is given', function () {
        render(<MoveControl rovers={rovers} moveSelectedRover={moveRover}/>)

        const commandInput = screen.getByLabelText('command-input')
        const moveBtn = screen.getByText('Move')

        expect(commandInput.value).toBe('')
        expect(moveBtn.disabled).toBeTruthy()
    });

})