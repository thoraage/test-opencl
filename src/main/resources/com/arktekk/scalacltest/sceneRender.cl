float sqr(const float v) {
  return v * v;
}

uchar hit(const float3 pos, const float3 dir, const float4 sphere) {
  const float a = sqr(dir.x) + sqr(dir.y) + sqr(dir.z);
  const float b = 2*dir.x*(pos.x - sphere.x) +  2*dir.y*(pos.y-sphere.y) +  2*dir.z*(pos.z-sphere.z);
  const float c = sqr(sphere.x) + sqr(sphere.y) + sqr(sphere.z) + sqr(pos.x) + sqr(pos.y) + sqr(pos.z) +
                                    -2*(sphere.x*pos.x + sphere.y*pos.y + sphere.z*pos.z) - sqr(sphere.w);
  if (sqr(b) - 4*a*c > 0)
    return 1;
  else
    return 0;
}

uchar render(const int x, const int y, const int width, const int height) {
  const float3 pos = (float3)(x - width / 2, y - height / 2, 0.0);
  const float3 dir = (float3)(0.0, 0.0, 1.0);
  const float4 sphere = (float4)(5.0, 5.0, 0.0, 15.0);
  return hit(pos, dir, sphere);
}

__kernel void sceneRender(const int width, const int workSize, __global uchar* output) {
  int workItem = get_global_id(0);
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    int x = pos % width;
    int y = pos / width;
    output[pos] = render(x, y, width, 30);
  }
}

/**

  x^2 + y^2 + z^2 = r^2

  z = sqrt(x^2 + y^2 - r^2)


*/