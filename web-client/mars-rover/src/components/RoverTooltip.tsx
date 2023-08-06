import React from 'react';

export const RoverTooltip = ({active, payload}: any) => {
    if (active && payload && payload.length) {
        const id = payload[0].payload.id
        let direction;
        switch(payload[0].payload.direction) {
            case "N": direction = "North"
                break;
            case "S": direction = "South"
                break;
            case "W": direction = "West"
                break;
            case "E": direction = "East"
                break;
            default: direction = "Undefined"
        }
        return (
            <div className="rover-tooltip">
                <p>{`Rover ID : ${id}`}</p>
                <p>{`X : ${payload[0].value}`}</p>
                <p>{`Y : ${payload[1].value}`}</p>
                <p>{`Direction : ${direction}`}</p>
            </div>
        );
    }
    return null
};