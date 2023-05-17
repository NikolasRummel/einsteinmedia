import React from 'react';
import LeftSidebar from "./main/LeftSidebar";
import RightSidebar from "./main/RightSidebar";
import Feed from "./main/Feed";

export default function LastPosts() {
    return (
        <>
            <br></br>
            <div className="container-fluid">
                <div className="row">
                    <div className="col-md-3 sticky-sidebar">
                        <LeftSidebar/>
                    </div>

                    <div className="col-md-6 ">
                        <Feed/>
                    </div>

                    <div className="col-md-3 sticky-sidebar">
                        <RightSidebar/>
                    </div>

                </div>
            </div>
        </>
    );
}