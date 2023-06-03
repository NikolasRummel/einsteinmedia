import React, {useEffect, useState} from "react";
import PostCard from "../../components/PostCard";
import {Card} from "react-bootstrap";

const Feed = () => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8081/posts")
            .then((response) => response.json())
            .then((data) => {
                setPosts(data);
            })
            .catch((error) => console.log(error));
    }, []);

    return (
        <>
            {posts.length === 0 ? (
                <Card className="text-center">
                    <Card.Body>
                        <Card.Title>
                            Hey, there are no posts yet!
                        </Card.Title>
                        <Card.Text>
                            Register or login to create the first post :)
                        </Card.Text>
                    </Card.Body>
                </Card>
            ) : (
                posts.map((item, index) => (
                    <div key={index}>
                        <PostCard
                            profileImage={item.author.profileImage}
                            userName={item.author.userName}
                            firstName={item.author.firstName}
                            lastName={item.author.lastName}
                            timestamp={item.post.timestamp}
                            headline={item.post.headline}
                            text={item.post.text}
                            postUniqueId={item.post.uniqueId}
                            userUniqueId={item.author.uniqueId}
                            imageLink={item.post.imageLink}
                        />
                    </div>
                ))
            )}
        </>
    );
}

export default Feed;
