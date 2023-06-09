# Early-Flood-Detection

The Early Flood Detection System is Java application enables you to receive and transmit information about water levels in rivers, names of measurement points, GPS coordinates of the water level measurement point and measurement date

## Features

- Create, update and delete flood Detectors
- Read from CSV files 
- Write to CSV files
- Retrieve flood Detectors by id

## Tecnologies Used

- Java
- Spring Framework
- Maven

## Installation

To run this project locally, follow these steps:
1. Clone the repository:
git clone https://github.com/Danvace/Early-Flood-Detection.git
2. Open project in your IDE
3. Build project with command : mvn clean install
4. Run the application with command : mvn spring-boot: run
5. The application will start running on `http://localhost:8080`.

## Usage

The following endpoints are available for interacting with the system:

- `GET /flood` : Retrieve a list of all flood Detectors.
- `GET /flood/{id}` : Retrieve a specific flood Detector by ID.
- `POST /flood` : Create a flood Detector(the body requires).
- `PUT /flood/{id}` : Update an existing flood Detector.
- `DELETE /flood/{id}` : Delete a flood Detector by ID.

## Data Storage

The application stores data in CSV files located in the `src/main/resources/floods` directory. Each file named with the format `FloodDetector-yyyy-mm-dd.csv`.

## Contributing

Contributions to this project are welcome. To contribute, follow these steps:

1. Fork the repository on GitHub.
2. Create a new branch with a descriptive name.
3. Make your changes and test them thoroughly.
4. Commit your changes with clear commit messages.
5. Push your branch to your forked repository.
6. Submit a pull request, describing your changes in detail and explaining why they should be merged.

## Contact

For any questions, please contact [vipdan055@gmail.com].
