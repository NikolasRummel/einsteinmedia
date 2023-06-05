import React from 'react';
import {FaClock} from "react-icons/fa";

function Comment({ profileImage, userName, text, timestamp }) {
    return (
        <div className="comment-box">
            <div className="comment">
                <div className="comment-user">
                    <img src={profileImage} alt="Profile Picture" className="comment-profile-image" />
                    <div className="comment-username">@{userName}</div>
                </div>
                <div className="text-muted h7 mb-2">
                    <span><FaClock style={{ marginRight: "3px", marginBottom: "3px" }} /></span>
                    {timestamp}
                </div>
            </div>
            <div >
                <p>{text}</p>
            </div>
        </div>
    );
}

function CommentsComponent({ comments }) {
    const handleDeleteComment = (commentId) => {
        // Hier kannst du die Logik zum Löschen eines Kommentars implementieren
    };

    return (
        <div className="comments">
            {comments.map((comment) => (
                <Comment
                    key={comment.comment.uniqueId}
                    profileImage={comment.author.profileImage}
                    userName={comment.author.userName}
                    text={comment.comment.text}
                    timestamp={comment.comment.timestamp}
                    onDeleteComment={() => handleDeleteComment(comment.commentId)}
                />
            ))}
        </div>
    );
}

export default CommentsComponent;
