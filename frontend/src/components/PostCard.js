import {FaClock, FaThumbsUp} from "react-icons/fa";

function PostCardComponent({profileImage, userName, firstName, lastName, timestamp, headline, text}) {
    return (
        <>
            <div className="card">
                <div className="card-header">
                    <div className="d-flex justify-content-between align-items-center">
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
                    </div>
                </div>
                <div className="card-body">
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
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