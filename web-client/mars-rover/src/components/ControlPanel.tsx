import React from 'react';
import CreateControl from "./CreateControl";
import MoveControl from "./MoveControl";
import {Rover} from "../model/Rover";

export interface ControlPanelProps {
    rovers: Rover[],
    addRover: (rover: Rover) => void
    moveSelectedRover: (rover: Rover) => void
}

const ControlPanel = ({rovers, addRover, moveSelectedRover}: ControlPanelProps) => {
    return (
        <div>
            <div>
                <CreateControl addRover={addRover} />
                <MoveControl rovers={rovers} moveSelectedRover={moveSelectedRover} />
            </div>
        </div>
    )
}

export default ControlPanel