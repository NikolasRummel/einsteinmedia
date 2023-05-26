import React from 'react';
import { MapContainer, TileLayer, Marker } from 'react-leaflet';

function Impressum() {
    const mapContainerStyle = {
        width: '100%',
        height: '400px',
    };

    const position = [49.0069, 8.4037]; // Replace with the latitude and longitude of your location

    return (
        <div className="container vh-100 d-flex align-items-center justify-content-center">
            <div className="card col-md-5">
                <div className="card-body">
                    <div className="d-flex justify-content-center align-items-center mt-4">
                        <h2 className="card-title fw-bold">Impressum</h2>
                    </div>
                    <h4> Information pursuant to § 5 TMG</h4>
                    <p>
                        Nikolas Rummel<br />
                        Wilhelm-Hausenstein-Allee 23A<br />
                        Karlsruhe, Germany
                    </p>
                    <p>
                        Phone: 0151/20188919<br />
                        Email: <u>hello@nikolas.rummel.de</u>
                    </p>
                    <p>
                        Supervisory Authority:<br />
                        Name of Supervisory Authority<br />
                        Street, House Number<br />
                        Postal Code, City<br />
                        Country
                    </p>
                    <div style={mapContainerStyle}>
                        <MapContainer center={position} zoom={14} style={mapContainerStyle}>
                            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                            <Marker position={position} />
                        </MapContainer>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Impressum;
