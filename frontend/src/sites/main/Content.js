import React from "react";
import Feed from "./Feed";
import NewPost from "../../components/NewPost";

export default function Maincontent() {
    return (
        <>
            <div className="col-md-6 gedf-main">
                <div className="card gedf-card">
                    <div className="card-header">
                      <NewPost></NewPost>
                    </div>
                </div>
                <br></br>
                <Feed/>
            </div>
        </>
    );
}
