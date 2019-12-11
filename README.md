# SM-Market-Simulation

A generative simulation model for a restaurant staff scheduler that maximizes average customer satisfaction through minimizing wait time and cost.

We implemented the [Bayesian data analysis method](https://youtu.be/3OJEae7Qb_o?t=277) and the [ABCmod (Activity-Based Conceptual modelling) framework](https://ieeexplore.ieee.org/document/7822082) to solve this problem.

In this project,
* We create a generative model that simulates the behaviour of a deli restaurant with two seperate counters, each of which can have multiple employees.
* We then use Bayesian Inference on the data (simulated by the generative model) to analyze the distribution of customer satisfaction.
The goal of the simulation is to determin the best schedule for employees, based on the frequency of clients throughout the day.

## Bayesian data analysis: An introduction
If you are unfamiliar with the Bayesian data analysis method and Monte Carlo simulation, [this video](https://youtu.be/3OJEae7Qb_o?t=277) presents a decent introduction.
