# Concurrent Boids
![Demo](./docs/images/demo.gif)

The repository contains the first assignment of the [Concurrent and Distributed Programming course](https://www.unibo.it/en/study/course-units-transferable-skills-moocs/course-unit-catalogue/course-unit/2024/412598) (Master's Degree in CSE @ Unibo). The project involves designing and developing a concurrent version of the original [boids simulation](https://en.wikipedia.org/wiki/Boids), as conceived by Craig Reynolds in 1986. The repository includes three distinct versions of the project, each designed and developed using a different approach:
- Multithreaded architecture (using default/platform threads)
- Task-based approach
- Virtual threads

### Documentation
Within the `docs/` directory, you will find a concise report detailing all the design and implementation decisions made during the development process. In particular:
- A brief analysis of the problem.
- A description of the adopted design, strategy and architecture.
- An explanation of the system's behaviour, using Petri Nets.
- A review of the performance of each version of the program, including a comparison with the sequential version.
- The verification of the multithreaded program (a model of it), conducted using Java Pathfinder (JPF).
