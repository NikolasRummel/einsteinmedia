import React, {useState} from 'react';
import * as authApi from "../api/authApi";
import Swal from "sweetalert2";

export const NewPost = ({onClose}) => {

    const [headline, setHeadline] = useState("");
    const [text, setText] = useState("");
    const [imageSource, setImageSource] = useState("");

    const newPostSubmit = (e) => {
        e.preventDefault();

        const requestOptions = {
            method: 'POST',
            headers: {
                'Authorization': authApi.getAuthKey(),
            },
            body: JSON.stringify({
                headline: headline,
                text: text,
                imageLink: imageSource,
            })
        };

        fetch("http://localhost:8081/posts", requestOptions)
            .then(res => {
                console.log(res)
                if (res.status === 200) {
                    Swal.fire({
                        icon: 'success',
                        title: `Successfully created a new post`,
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true,
                        background: "#ffffff"

                    });

                } else {

                }
            }).catch(ex => {
            console.log("ex:" + ex)
        })

        onClose()

    };

    const closeButton = () => {
        onClose();
    }

    return (
        <div className="newpost-container">
            <div className="newpost">
                <div className="card-header d-flex justify-content-end">
                    <button type="button" className="btn-close" aria-label="Close" onClick={closeButton}></button>
                </div>
                <div className="card-body">
                    <h2 className="card-title">Create new post</h2>
                    <br/>
                    <form id="loginform" onSubmit={newPostSubmit}>
                        <div className="form-group">
                            <input
                                type="text"
                                className="form-control"
                                id="HeadlineInput"
                                name="HeadlineInput"
                                aria-describedby=""
                                placeholder="Headline of your post"
                                onChange={(event) => setHeadline(event.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label></label>
                            <input
                                type="text"
                                className="form-control"
                                id="ImageSourceInput"
                                name="ImageSourceInput"
                                aria-describedby=""
                                placeholder="optionally, you can add an image to your post"
                                onChange={(event) => setImageSource(event.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label></label>
                            <textarea
                                type="text"
                                className="form-control"
                                id="TextInput"
                                name="TextInput"
                                aria-describedby=""
                                placeholder="Text of your post"
                                onChange={(event) => setText(event.target.value)}
                            />
                        </div>
                        <br/>
                        <button type="submit" onSubmit={newPostSubmit} className="btn btn-primary">
                            Submit
                        </button>

                    </form>
                </div>
            </div>
        </div>
    );
}