import React, {useState} from 'react';

export default function NewPost() {

    const [headline, setHeadline] = useState("");
    const [text, setText] = useState("");
    const [imageSource, setImageSource] = useState("");

    const newPostSubmit = (e) => {
        e.preventDefault();
    };

    return (
        <div>
            <h5 className="card-title">Create new post</h5>
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
                <button type="submit" className="btn btn-primary">
                    Submit
                </button>
            </form>
        </div>
    );
}