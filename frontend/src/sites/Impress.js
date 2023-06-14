import React from 'react';

function Impressum() {

    return (
        <div className="container vh-100 d-flex align-items-center justify-content-center">
            <div className="card col-md-6">
                <div className="card-body">
                    <div className="d-flex justify-content-center align-items-center mt-4">
                        <h2 className="card-title fw-bold">Impress</h2>
                    </div>
                    <h4 className="mt-4 mb-1"> Information pursuant to § 5 TMG</h4>
                    <p>
                        Nikolas Rummel<br />
                        Wilhelm-Hausenstein-Allee 23A<br />
                        Karlsruhe, Germany
                    </p>
                    <p>
                        Phone: 0151/20188919<br />
                        Email: <u>hello@nikolas.rummel.de</u>
                    </p>
                </div>

            </div>
        </div>
    );
}

export default Impressum;
