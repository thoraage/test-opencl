__kernel void sceneRender(
    const int width,
    const int workSize,
    __global uchar* output)
{
  int workItem = get_global_id(0);
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    output[pos] = (workItem + workSize) % 10;
  }
}