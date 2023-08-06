import React, {useEffect, useState} from 'react';
import Map from "./Map";
import ControlPanel from "./ControlPanel";
import {Rover} from "../model/Rover";
import {getAllRovers} from "../http/RoverRequests";
import Header from "./Header";
import Divider from "./Divider";

const Dashboard = () => {
    const [rovers, setRovers] = useState<Rover[]>([])

    useEffect(() => {
        getAllRovers(setRovers)
    }, [])

    const addRover = (rover: Rover) => setRovers([
        ...rovers,
        rover
    ])

    const moveSelectedRover = (rover: Rover) => setRovers([
        ...rovers.filter((r) => r.id !== rover.id),
        rover
    ])

    return (
        <div>
            <Header/>
            <Divider/>
            <Map rovers={rovers} />
            <Divider/>
            <ControlPanel rovers={rovers} addRover={addRover} moveSelectedRover={moveSelectedRover}/>
        </div>
    )
}

export default Dashboard