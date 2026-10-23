# Q14. AWS: ECS on EC2 vs ECS on Fargate

**Duration:** 10-minute verbal answer · **Source:** IDFC Technical Round 1, per LeetCode ("why use Fargate over ECS?", asked again after the first answer)

## Question

"Your Spring Boot microservice runs on ECS. Why would you choose the **Fargate** launch type over the **EC2** launch type, and when would you not?"

## The catch

Fargate isn't an alternative to ECS. It's a **launch type of ECS** (and of EKS). The real comparison is **ECS on EC2** (you manage the instances) against **ECS on Fargate** (serverless compute for containers). Say this first; it's probably why the interviewer asked again.

## What a complete answer covers

| Dimension | ECS on EC2 | ECS on Fargate |
|---|---|---|
| Who manages servers | You: AMI, patching, the ECS agent, capacity providers and auto scaling groups | AWS; you only declare task CPU and memory |
| Scaling | Two layers: tasks *and* instances (cluster auto scaling) | Task-level only |
| Pricing | Per instance; cheaper at steady, high use and with Spot or Savings Plans | Per vCPU-second and GB-second; a premium for convenience, but Fargate Spot exists |
| Bin-packing | You can pack many tasks onto one instance | Each task is isolated in its own micro-VM |
| Control | GPUs, custom kernels, daemon tasks, host volumes, privileged mode | No GPU, no daemons, limited host access |
| Start time | Fast if capacity is already warm | Image pull on every start. Slower; reduce with smaller images |
| Security | Shared host; you harden it | Strong task isolation, smaller attack surface |

**Good answer shape:** "For a typical stateless microservice with spiky traffic and a small team, I'd choose Fargate: no instance management, task-level scaling, and better isolation. I'd choose EC2 for steady high use where cost matters, or when I need GPUs, daemon tasks or host-level tuning."

## Likely follow-ups

1. Fargate vs **Lambda** for the same service: run length (Lambda's limit is 15 minutes), cold starts, keeping connection pools, cost at high requests per second.
2. How you would auto-scale the service: target tracking on CPU or memory, or on request count per target, through Application Auto Scaling.
3. The scaling policy you used in your own project, and why. (This was asked in another IDFC round.)
4. Where secrets live (Secrets Manager or SSM injected into the task definition) and the IAM task role vs the execution role.

> Relate this to your own AWS experience (Lambda, SQS, Kinesis, IoT Core, API Gateway, CloudWatch). Don't claim hands-on production experience with ECS, Docker or Kubernetes. Say you understand the trade-offs and have worked mostly on the serverless and messaging side.
