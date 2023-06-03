import React from 'react';
import LeftSidebar from './main/LeftSidebar';
import RightSidebar from './main/RightSidebar';
import Feed from './main/Feed';
import PostCard from "../components/PostCard";

export default function LastPosts() {
    return (
        <>
            <br />
            <div className="container-fluid" style={{ minHeight: '80vh', display: 'flex', flexDirection: 'column' }}>
                <div className="row flex-grow-1">
                    <div className="col-md-3 sticky-sidebar">
                        <LeftSidebar />
                    </div>
                    <div className="col-md-6">
                        <Feed />
                    </div>
                    <div className="col-md-3 sticky-sidebar">
                        <RightSidebar />
                    </div>
                </div>
            </div>
        </>
    );
}
