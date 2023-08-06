import React from "react";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import {Rover} from "../../model/Rover";
import {Direction} from "../../model/Direction";
import {rootUrl} from "../../http/RoverRequests";
import {render, waitFor} from "@testing-library/react";
import Dashboard from "../../components/Dashboard";

describe('Dashboard Component', () => {

    let mock: MockAdapter;

    beforeAll(() => {
        mock = new MockAdapter(axios)
    })

    afterEach(() => {
        mock.reset()
    })

    it('should trigger getAllRovers upon load and loads all rovers into map', async function () {
        const expectedUrl = `${rootUrl}/api/v1/allRovers`
        const rovers: Rover[] = [{id: "R1", coordinate: {x: 1, y: 1, direction: Direction.E}}]
        mock.onGet(expectedUrl).reply(200, rovers)

        const {container} = render(<Dashboard/>);
        await waitFor(() => {
            const loadedRovers = container.getElementsByClassName("recharts-scatter-symbol")

            expect(mock.history.get[0].url).toEqual(expectedUrl)
            expect(loadedRovers.length).toEqual(1)
        })
    });

})