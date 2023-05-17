import React from "react";
import * as authApi from "../../api/authApi"

export default function Rightsidebar() {

    const user = authApi.getUser();

    return (
        <>
            {!authApi.isLoggedIn() && (
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">New at Einsteinmedia?</h5>
                        <h6 className="card-subtitle mb-2 text-muted">Create a new account or log in </h6>
                        <a href="/register" className="card-link">
                            Create new account
                        </a>
                        <a href="/login" className="card-link">
                            Login to my account
                        </a>
                    </div>
                </div>
            )}
            {
                authApi.isLoggedIn() && (
                    <div className="card">
                        <div className="card-body">
                            <div className="h5">@{user.userName}</div>
                            <div className="h7 text-muted">Fullname: {user.firstName} {user.lastName}</div>
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
                )
            }
            <br/>
            <iframe src="https://discord.com/widget?id=615119175127400458&theme=dark" width="410" height="750"
                    frameBorder="0"
                    sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>
        </>
    );
}
