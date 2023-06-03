import {FaClock, FaThumbsUp, FaTrash} from "react-icons/fa";
import * as authApi from "../api/authApi";
import Swal from "sweetalert2";
import {getAuthKey} from "../api/authApi";
import {HttpStatusCode} from "axios";
import {Button, Card, Image, OverlayTrigger, Tooltip} from "react-bootstrap";
import React, {useEffect, useState} from 'react';
import {Navigate, useHistory, useNavigate} from "react-router-dom";

function PostCardComponent({
                               profileImage,
                               userName,
                               firstName,
                               lastName,
                               timestamp,
                               headline,
                               text,
                               postUniqueId,
                               userUniqueId,
                               imageLink
                           }) {

    const [isCardVisible, setIsCardVisible] = useState(false);
    const [showToolTip, setShowToolTip] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const currentPath = window.location.pathname;
        if (currentPath.includes("profile/visit")) {
            setShowToolTip(false)
        }

        if (authApi.getUser() != null) {
            if (userUniqueId === authApi.getUser().uniqueId) {
                setShowToolTip(false)
            }
        }

    });

    const deletePost = () => {
        Swal.fire({
            icon: 'warning',
            title: 'Deleting a post',
            text: 'Are you sure to delete this post?',
            showConfirmButton: true,
        }).then((result) => {
            let path = "http://localhost:8081/posts/delete/" + postUniqueId;
            console.log("PATH: " + path + "auth - " + getAuthKey())
            fetch(path, {
                method: "POST", //else problems in backend
                headers: {
                    'Authorization': getAuthKey(),
                    'Content-Type': 'text/plain'
                },
            }).then(result => {
                if (result.status == HttpStatusCode.Ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Deleted post',
                        text: 'Successfully deleted the post ' + headline,
                        showConfirmButton: true,
                    }).then((result) => {
                        window.location.reload();
                    });

                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error by deleting a post',
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 2000,
                        timerProgressBar: true,
                        background: "#ffffff"
                    });
                }
            });
        });
    }

    const renderCloseButton = () => {
        let user = authApi.getUser();

        if (user === null) return null;

        if (user.userName === userName) {
            return <FaTrash className={"delete-post"} onClick={deletePost}/>
        }
    }

    const renderImageLink = () => {
        console.log(imageLink + " #+#+#+#+")
        if (imageLink !== null && imageLink !== undefined && imageLink !== "") {
            return <img src={imageLink} className="w-100" alt='Posting Image' />;
        }
    }


    function visitProfile(userId) {
        navigate('/profile/visit?userId=' + userId);
    }

    const renderVisitProfileButton = (userId) => {
        return <Button onClick={() => visitProfile(userId)}>Visit this user</Button>;
    };

    return (
        <>
            <div className="card">
                <div
                    className="card-header d-flex justify-content-between align-items-center"
                    onMouseEnter={() => setIsCardVisible(true)}
                    onMouseLeave={() => setIsCardVisible(false)}
                >
                    <div className="d-flex justify-content-between align-items-center post-user">
                        {isCardVisible && showToolTip && (
                            <div className="tooltip-card">
                                <div className="ml-2">
                                    <div className="h5 m-0">@{userName}</div>
                                    <div className="h7 text-muted">followed</div>
                                </div>
                                <br/>
                                {renderVisitProfileButton(userUniqueId)}
                            </div>
                        )}
                        <div className="mr-2">
                            <img
                                className="rounded-circle"
                                width="50"
                                height="50"
                                src={profileImage}
                                alt="Profile Picture"
                            />
                        </div>
                        <div className="ml-2">
                            <div className="h5 m-0">@{userName}</div>
                            <div className="h7 text-muted">{firstName} {lastName}</div>
                        </div>
                    </div>
                    <div className="d-flex justify-content-end">
                        {renderCloseButton()}
                    </div>
                </div>
                <div className="card-body">
                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <h5 className="card-title">{headline}</h5>
                        <div className="text-muted h7 mb-2">
                            <span><FaClock style={{marginRight: "3px", marginBottom: "3px"}}/></span>
                            {timestamp}
                        </div>
                    </div>
                    {renderImageLink()}
                    <p className="card-text">{text}</p>
                </div>
                <div className="card-footer">
                    <a href="#" className="card-link">
                        <FaThumbsUp/> Like
                    </a>
                </div>
            </div>
            <br/>
        </>
    );
}

export default PostCardComponent;