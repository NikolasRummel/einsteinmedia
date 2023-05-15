import {FaClock, FaRegClock, FaThumbsUp} from "react-icons/fa";

function PostCardComponent() {
    return (
        <>
            <div className="card">
                <div className="card-header">
                    <div className="d-flex justify-content-between align-items-center">
                        <div className="d-flex justify-content-between align-items-center">
                            <div className="mr-2">
                                <img
                                    className="rounded-circle"
                                    width="45"
                                    src="https://picsum.photos/50/50"
                                    alt="Profile Picture"
                                />
                            </div>
                            <div className="ml-2">
                                <div className="h5 m-0">@CubePixels</div>
                                <div className="h7 text-muted">Nikolas Rummel</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="card-body">
                    <div className="text-muted h7 mb-2">
                        {" "}
                        Di. 4. Apr. 22:05
                    </div>
                    <a className="card-link" href="#">
                        <h5 className="card-title">
                            This is a post title
                        </h5>
                    </a>

                    <p className="card-text">
                        Lorem ipsum dolor sit amet consectetur adipisicing elit. Quo
                        recusandae nulla rem eos ipsa praesentium esse magnam nemo dolor
                        sequi fuga quia quaerat cum, obcaecati hic, molestias minima iste
                        voluptates.
                    </p>
                </div>
                <div className="card-footer">
                    <a href="#" className="card-link">
                       <FaThumbsUp /> Like
                    </a>
                </div>
            </div>
            <br />
        </>
    );
}

export default PostCardComponent;