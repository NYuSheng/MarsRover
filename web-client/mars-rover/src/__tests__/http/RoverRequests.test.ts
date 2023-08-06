import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import {Direction} from "../../model/Direction";
import {Rover} from "../../model/Rover";
import {createRover, getAllRovers, moveRover, rootUrl} from "../../http/RoverRequests";
import {CreateRoverRequest} from "../../components/CreateControl";
import {MoveRoverRequest} from "../../components/MoveControl";

describe('Rover Requests', () => {

    let mock: MockAdapter;

    beforeAll(() => {
        mock = new MockAdapter(axios)
    })

    afterEach(() => {
        mock.reset()
    })

    describe('getAllRovers', () => {

        const expectedUrl = `${rootUrl}/api/v1/allRovers`

        it('should trigger success cb with api response for getAllRovers', async function () {
            const rovers: Rover[] = [{id: "R1", coordinate: {x: 1, y: 1, direction: Direction.E}}]
            const mockCb = jest.fn()

            mock.onGet(expectedUrl).reply(200, rovers)

            await getAllRovers(mockCb)
            expect(mock.history.get[0].url).toEqual(expectedUrl)
            expect(mockCb).toBeCalledWith(rovers)
        });

        it('should trigger windows alert with error message for getAllRovers', async function () {
            jest.spyOn(window, 'alert').mockImplementation(() => {});

            const errorMessage = "Error message"
            const mockCb = jest.fn()

            mock.onGet(expectedUrl).reply(500, {message: errorMessage})

            await getAllRovers(mockCb)
            expect(mock.history.get[0].url).toEqual(expectedUrl)
            expect(window.alert).toBeCalledWith(`Error encountered during fetching all rovers. ${errorMessage}`)
        });

    })

    describe('createRover', () => {

        const expectedUrl = `${rootUrl}/api/v1/createRover?createRequest=3,4,E`
        const createRoverRequest: CreateRoverRequest = {
            x: 3,
            y: 4,
            direction: "E"
        }

        it('should trigger success cb with api response for createRover', async function () {
            jest.spyOn(window, 'alert').mockImplementation(() => {});

            const rover: Rover = {id: "R1", coordinate: {x: 3, y: 4, direction: Direction.E}}
            const mockCb = jest.fn()

            mock.onPost(expectedUrl).reply(200, rover)

            await createRover(createRoverRequest, mockCb)
            expect(mock.history.post[0].url).toEqual(expectedUrl)
            expect(window.alert).toBeCalledWith("Rover R1 successfully created!")
            expect(mockCb).toBeCalledWith(rover)
        });

        it('should trigger windows alert with error message for createRover', async function () {
            jest.spyOn(window, 'alert').mockImplementation(() => {});

            const errorMessage = "Error message"
            const mockCb = jest.fn()

            mock.onPost(expectedUrl).reply(500, {message: errorMessage})

            await createRover(createRoverRequest, mockCb)
            expect(mock.history.post[0].url).toEqual(expectedUrl)
            expect(window.alert).toBeCalledWith(`Error encountered during rover creation. ${errorMessage}`)
        });

    })

    describe('moveRover', () => {

        const expectedUrl = `${rootUrl}/api/v1/moveRover?roverId=R1&moveCommands=f,f,b`
        const moveRoverRequest: MoveRoverRequest = {
            id: "R1",
            command: "f,f,b"
        }

        it('should trigger success cb with api response for moveRover', async function () {
            jest.spyOn(window, 'alert').mockImplementation(() => {});

            const rover: Rover = {id: "R1", coordinate: {x: 3, y: 4, direction: Direction.E}}
            const mockCb = jest.fn()

            mock.onPut(expectedUrl).reply(200, rover)

            await moveRover(moveRoverRequest, mockCb)
            expect(mock.history.put[0].url).toEqual(expectedUrl)
            expect(window.alert).toBeCalledWith("Rover R1 successfully moved!")
            expect(mockCb).toBeCalledWith(rover)
        });

        it('should trigger windows alert with error message for moveRover', async function () {
            jest.spyOn(window, 'alert').mockImplementation(() => {});

            const errorMessage = "Error message"
            const mockCb = jest.fn()

            mock.onPut(expectedUrl).reply(500, {message: errorMessage})

            await moveRover(moveRoverRequest, mockCb)
            expect(mock.history.put[0].url).toEqual(expectedUrl)
            expect(window.alert).toBeCalledWith(`Error encountered during rover move operation. ${errorMessage}`)
        });

    })

})