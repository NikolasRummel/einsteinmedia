import React from 'react';

function Comment({ profileImage, userName, text }) {
    return (
        <div className="comment-box">
            <div className="comment">
                <div className="comment-user">
                    <img src={profileImage} alt="Profile Picture" className="comment-profile-image" />
                    <div className="comment-username">@{userName}</div>
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
                    key={comment.commentId}
                    profileImage={comment.profileImage}
                    userName={comment.userName}
                    text={comment.text}
                    onDeleteComment={() => handleDeleteComment(comment.commentId)}
                />
            ))}
        </div>
    );
}

export default CommentsComponent;
