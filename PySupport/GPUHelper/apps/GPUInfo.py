from typing import Any

import GPUtil


def get_all_gpus() -> list[Any]:
    gpus = GPUtil.getGPUs()
    return gpus


def _show_utilization_info_all() -> None:
    GPUtil.showUtilization(all=True)


def _show_gpu_info(gpu: Any, seq: int) -> None:
    print(f"{seq}. Name: {gpu.name}, Load: {gpu.load * 100}%, Temp: {gpu.temperature}C")


def _show_info_for_all_gpus() -> None:
    seq = 0
    for gpu in get_all_gpus():
        _show_gpu_info(gpu, ++seq)


def _show_available_gpus() -> None:
    device_ids: list[Any] = GPUtil.GPUtil.getFirstAvailable()
    if device_ids is not None and len(device_ids) > 0:
        for gpu_id in device_ids:
            seq = 0
            print(f"{++seq}. ID: {gpu_id}")
    else:
        print("No Available GPUs")


def main():
    print("GPU Utilization")
    _show_utilization_info_all()
    print("\nAll GPUs")
    _show_info_for_all_gpus()
    print("\nGPU Description")
    print("7.5 Compute;  Data Center: NVIDIA T4;  Workstation: GeForce RTX 2080")
    print("\nShow Available GPUs")
    _show_available_gpus()
    print("\nDone")


if __name__ == "__main__":
    main()
