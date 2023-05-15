import React from "react";

export default function Leftsidebar() {
    return (
        <>
            <div className="col-md-3">
                <div className="card">
                    <div className="card-body">
                        <div className="h5">@CubePixels</div>
                        <div className="h7 text-muted">Fullname : Nikolas Rummel</div>
                        <div className="h7">
                            Description of me is currently in work
                        </div>
                    </div>
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item">
                            <div className="h6 text-muted">Followers</div>
                            <div className="h5">5.2342</div>
                        </li>
                        <li className="list-group-item">
                            <div className="h6 text-muted">Following</div>
                            <div className="h5">6758</div>
                        </li>
                    </ul>
                </div>
            </div>
        </>
    );
}
