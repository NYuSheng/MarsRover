import React, {useState} from 'react';
import "../css/MoveControl.css";
import {Rover} from "../model/Rover";
import {MoveCommand} from "../model/MoveCommand";
import {moveRover} from "../http/RoverRequests";

export interface MoveRoverRequest {
    id: string,
    command: string
}

export interface MoveControlProps {
    rovers: Rover[],
    moveSelectedRover: (rover: Rover) => void
}

const MoveControl = ({rovers, moveSelectedRover}: MoveControlProps) => {

    const newMoveRequest: MoveRoverRequest = {
        id: "",
        command: ""
    }

    const [moveRequest, setMoveRequest] = useState(newMoveRequest)

    const onRoverChange = (event: any) => {
        setMoveRequest(
            {
                ...moveRequest,
                id: event.target.value
            }
        )
    }

    const onMoveClick = (command: MoveCommand) => {
        setMoveRequest(
            {
                ...moveRequest,
                command: !moveRequest.command ? `${command}` : `${moveRequest.command},${command}`
            }
        )
    }

    const onCommandClear = () => {
        setMoveRequest(
            {
                ...moveRequest,
                command: ""
            }
        )
    }

    const onMove = () => {
        moveRover(moveRequest, moveSelectedRover)
        setMoveRequest(newMoveRequest)
    }

    const isMoveDisabled = () => {
        return !moveRequest.id || !moveRequest.command
    }

    return (
        <div className="move-container">
            <div className="move-label">Move rover</div>
            <div className="move-inputs">
                <div className="move-input">
                    <label>Select rover:</label>
                    <select aria-label="rover-select" name="rover" id="rover" value={moveRequest.id} onChange={onRoverChange}>
                        <option value="">Select</option>
                        {rovers.map((rover) => {
                            return (
                                <option value={`${rover.id}`} key={`${rover.id}`}>{rover.id}</option>
                            )
                        })}
                    </select>
                </div>
                <div className="move-input">
                    <label>Command:</label>
                    <div className="move-control-btns">
                        <button onClick={() => onMoveClick(MoveCommand.F)}>Front</button>
                        <button onClick={() => onMoveClick(MoveCommand.B)}>Back</button>
                        <button onClick={() => onMoveClick(MoveCommand.R)}>Right</button>
                        <button onClick={() => onMoveClick(MoveCommand.L)}>Left</button>
                    </div>
                    <input aria-label="command-input" className="move-command-input" disabled={true} value={moveRequest.command}/>
                    <button onClick={onCommandClear}>Clear</button>
                </div>
            </div>
            <button className="move-btn" disabled={isMoveDisabled()} onClick={onMove}>Move</button>
        </div>
    )
}

export default MoveControl