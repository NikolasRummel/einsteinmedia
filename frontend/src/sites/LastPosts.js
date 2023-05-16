import React from 'react';
import LeftSidebar from "./main/LeftSidebar";
import RightSidebar from "./main/RightSidebar";
import MainContent from "./main/Content";

export default function LastPosts() {
    return (
        <>
            <br></br>
            <div className="container-fluid">
                <div className="row">
                    <LeftSidebar/>
                    <MainContent/>
                    <RightSidebar/>
                </div>
            </div>
        </>
    );
}