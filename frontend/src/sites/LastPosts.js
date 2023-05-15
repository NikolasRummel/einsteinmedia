import React from 'react';
import Leftsidebar from "./main/LeftSidebar";
import Rightsidebar from "./main/RightSidebar";
import Maincontent from "./main/Content";

export default function LastPosts() {
    return (
        <>
            <br></br>
            <div className="container-fluid">
                <div className="row">
                    <Leftsidebar/>
                    <Maincontent/>
                    <Rightsidebar/>
                </div>
            </div>
        </>
    );
}