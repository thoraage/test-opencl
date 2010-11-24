__kernel void calcPi(
    const int start,
    const int end,
    const int iterations,
    __global float* output)
{
  int part = get_global_id(0);
  float val = 0.0;
  for (int i = 0; i < iterations; i += 2) {
    int n = i + (part * iterations) + 1;
    if (n % 4 == 1)
      val += 1.0 / n;
    else
      val -= 1.0 / n;
  }
  output[part] = val;
}