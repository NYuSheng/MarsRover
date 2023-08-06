import React from 'react';
import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import CreateControl from "../../components/CreateControl";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import {Rover} from "../../model/Rover";
import {Direction} from "../../model/Direction";
import {rootUrl} from "../../http/RoverRequests";

describe('CreateControl Component', () => {

    const addRover = jest.fn()

    let mock: MockAdapter;

    beforeAll(() => {
        mock = new MockAdapter(axios)
    })

    afterEach(() => {
        mock.reset()
    })

    it('should have default inputs upon load', function () {
        render(<CreateControl addRover={addRover}/>)

        const xInput = screen.getByLabelText('x-input')
        const yInput = screen.getByLabelText('y-input')
        const directionInput = screen.getByLabelText('direction-select')

        expect(xInput.value).toBe('0')
        expect(yInput.value).toBe('0')
        expect(directionInput.value).toBe('N')
    });

    it('should be able to create rover based on the given inputs', async function () {
        jest.spyOn(window, 'alert').mockImplementation(() => {
        });
        const rover: Rover = {id: "R1", coordinate: {x: 5, y: 9, direction: Direction.E}}
        const expectedUrl = `${rootUrl}/api/v1/createRover?createRequest=5,9,E`
        mock.onPost(expectedUrl).reply(200, rover)

        render(<CreateControl addRover={addRover}/>)

        const xInput = screen.getByLabelText('x-input')
        const yInput = screen.getByLabelText('y-input')
        const directionInput = screen.getByLabelText('direction-select')
        const createBtn = screen.getByText('Create')

        fireEvent.change(xInput, {target: {value: 5}})
        fireEvent.change(yInput, {target: {value: 9}})
        fireEvent.change(directionInput, {target: {value: "E"}})
        fireEvent.click(createBtn)

        await waitFor(() => {
            expect(mock.history.post[0].url).toEqual(expectedUrl)
            expect(addRover).toBeCalledWith(rover)
        })
    });

    it('should disable create button if x coordinate input is empty', function () {
        render(<CreateControl addRover={addRover}/>)

        const xInput = screen.getByLabelText('x-input')
        const createBtn = screen.getByText('Create')

        fireEvent.change(xInput, {target: {value: ''}})
        expect(createBtn.disabled).toBeTruthy()
    });

    it('should disable create button if y coordinate input is empty', function () {
        render(<CreateControl addRover={addRover}/>)

        const yInput = screen.getByLabelText('y-input')
        const createBtn = screen.getByText('Create')

        fireEvent.change(yInput, {target: {value: ''}})
        expect(createBtn.disabled).toBeTruthy()
    });

})