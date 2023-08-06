import React from 'react';
import {CartesianGrid, Scatter, ScatterChart, Tooltip, XAxis, YAxis} from "recharts";
import "../css/Map.css";
import {Rover} from "../model/Rover";
import {RoverTooltip} from "./RoverTooltip";

export interface MapProps {
    rovers: Rover[],
}

const Map = ({rovers}: MapProps) => {

    const getData = () => {
        return rovers.map((rover) => ({
            id: rover.id,
            x: rover.coordinate.x,
            y: rover.coordinate.y,
            direction: rover.coordinate.direction
        }))
    }

    return (
        <div className="map-container">
            <div className="north-label">North</div>
            <div style={{display: "flex", justifyContent: "center"}}>
                <div className="west-label">West</div>
                <ScatterChart width={700} height={500}>
                    <CartesianGrid/>
                    <XAxis tickCount={3} type="number" dataKey="x" name="x" domain={[-100, 100]}/>
                    <YAxis tickCount={3} type="number" dataKey="y" name="y" domain={[-100, 100]}/>
                    <Tooltip content={<RoverTooltip/>}/>
                    <Scatter name="A school" data={getData()} fill="#8884d8"/>
                </ScatterChart>
                <div className="east-label">East</div>
            </div>
            <div className="south-label">South</div>
        </div>
    )
}

export default Map