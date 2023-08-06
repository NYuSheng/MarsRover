import React, {useState} from 'react';
import "../css/CreateControl.css";
import {createRover} from "../http/RoverRequests";
import {Rover} from "../model/Rover";

export interface CreateRoverRequest {
    x: number,
    y: number,
    direction: string
}

export interface CreateControlProps {
    addRover: (rover: Rover) => void
}

const CreateControl = ({addRover}: CreateControlProps) => {

    const newRoverRequest: CreateRoverRequest = {
        x: 0,
        y: 0,
        direction: "N"
    }

    const [roverRequest, setRoverRequest] = useState(newRoverRequest)

    const onXChange = (event: any) => {
        setRoverRequest(
            {
                ...roverRequest,
                x: event.target.value
            }
        )
    }

    const onYChange = (event: any) => {
        setRoverRequest(
            {
                ...roverRequest,
                y: event.target.value
            }
        )
    }

    const onDirectionChange = (event: any) => {
        setRoverRequest(
            {
                ...roverRequest,
                direction: event.target.value
            }
        )
    }

    const onCreate = () => {
        createRover(roverRequest, addRover)
        setRoverRequest(newRoverRequest)
    }

    const isCreateDisabled = () => {
        return !roverRequest.direction ||
            (!roverRequest.x && roverRequest.x !== 0) ||
            (!roverRequest.y && roverRequest.y !== 0)
    }

    return (
        <div className="create-container">
            <div className="create-label">Create rover</div>
            <div className="create-inputs">
                <div className="create-input">
                    <label>X-Coordinate:</label>
                    <input aria-label="x-input" type={"number"} value={roverRequest.x} onChange={onXChange}/>
                </div>
                <div className="create-input">
                    <label>Y-Coordinate:</label>
                    <input aria-label="y-input" type={"number"} value={roverRequest.y} onChange={onYChange}/>
                </div>
                <div className="create-input">
                    <label>Direction:</label>
                    <select aria-label="direction-select" name="direction" id="direction" value={roverRequest.direction} onChange={onDirectionChange}>
                        <option value="N">North</option>
                        <option value="S">South</option>
                        <option value="E">East</option>
                        <option value="W">West</option>
                    </select>
                </div>
            </div>
            <button className="create-btn" onClick={onCreate} disabled={isCreateDisabled()}>Create</button>
        </div>
    )
}

export default CreateControl