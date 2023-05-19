import {FaClock, FaThumbsUp, FaTrash} from "react-icons/fa";
import {Button, Tooltip} from "react-bootstrap";
import * as authApi from "../api/authApi";
import Swal from "sweetalert2";

function PostCardComponent({profileImage, userName, firstName, lastName, timestamp, headline, text}) {

    const deletePost = () => {
        Swal.fire({
            icon: 'warning',
            title: 'Deleting a post',
            text: 'Are you sure to delete this post?',
            showConfirmButton: true,
        }).then((result) => {
            Swal.fire({
                icon: 'success',
                title: 'Successfully deleted a post',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                background: "#ffffff"
            });
        });
    }

    const renderCloseButton = () => {
        let user = authApi.getUser();

        if(user === null) return null;

        if (user.userName === userName) {
            return <FaTrash className={"delete-post"} onClick={deletePost}/>
        }
    }

    return (
        <>
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <div className="d-flex justify-content-between align-items-center">
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
                        <h5 className="card-title">
                            {headline}
                        </h5>
                        <div className="text-muted h7 mb-2">
                            <span><FaClock style={{marginRight: "3px", marginBottom: "3px"}}/></span>
                            {timestamp}
                        </div>
                    </div>
                    <p className="card-text">
                        {text}
                    </p>
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