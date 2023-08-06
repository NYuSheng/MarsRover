import {CreateRoverRequest} from "../components/CreateControl";
import axios from "axios";
import {Rover} from "../model/Rover";
import {MoveRoverRequest} from "../components/MoveControl";

export const rootUrl = "http://localhost:8355"

export const getAllRovers = async (successCb: (rovers: Rover[]) => void) => {
    const url = `${rootUrl}/api/v1/allRovers`

    await axios.get(url)
        .then((response) => {
            successCb(response.data)
        })
        .catch((error) => {
            alert(`Error encountered during fetching all rovers. ${error.response.data.message}`)
        })
}

export const createRover = async (request: CreateRoverRequest, successCb: (rover: Rover) => void) => {
    const requestString = `${request.x},${request.y},${request.direction}`
    const url = `${rootUrl}/api/v1/createRover?createRequest=${requestString}`

    await axios.post(url)
        .then((response) => {
            const rover = response.data
            alert(`Rover ${rover.id} successfully created!`)
            successCb(rover)
        })
        .catch((error) => {
            alert(`Error encountered during rover creation. ${error.response.data.message}`)
        })
}

export const moveRover = async (request: MoveRoverRequest, successCb: (rover: Rover) => void) => {
    const url = `${rootUrl}/api/v1/moveRover?roverId=${request.id}&moveCommands=${request.command}`

    await axios.put(url)
        .then((response) => {
            const rover = response.data
            alert(`Rover ${rover.id} successfully moved!`)
            successCb(rover)
        })
        .catch((error) => {
            alert(`Error encountered during rover move operation. ${error.response.data.message}`)
        })
}