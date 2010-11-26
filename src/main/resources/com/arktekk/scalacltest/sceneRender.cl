float sqr(const float v) {
  return v * v;
}

float hit(const float3 pos, const float3 dir, const float4 sphere) {
  const float a = sqr(dir.x) + sqr(dir.y) + sqr(dir.z);
  const float b = 2*dir.x*(pos.x - sphere.x) +  2*dir.y*(pos.y-sphere.y) +  2*dir.z*(pos.z-sphere.z);
  const float c = sqr(sphere.x) + sqr(sphere.y) + sqr(sphere.z) + sqr(pos.x) + sqr(pos.y) + sqr(pos.z) +
                                    -2*(sphere.x*pos.x + sphere.y*pos.y + sphere.z*pos.z) - sqr(sphere.w);
  const float p1 = sqr(b) - 4*a*c;
  if (p1 > 0) {
    const float t1 = (-b - sqrt(p1)) / (2*a);
    const float t2 = (-b + sqrt(p1)) / (2*a); 
    if (t1 > 0 && t2 > 0)
      return (t1 < t2) ? t1 : t2;
    else
      return -1.0;
  } else
    return -1.0;
}

float3 normal(const float3 pos, const float3 dir, const float t, const float4 sphere) {
  const float3 hitPoint = (float3)(pos.x + t*dir.x, pos.y + t*dir.y, pos.z + t*dir.z);
  return (float3)(hitPoint.x - sphere.x, hitPoint.y - sphere.y, hitPoint.z - sphere.z);
}

float3 vectSub(const float3 v1, const float3 v2) {
  return (float3)(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
}

float3 vectConstMul(const float c, const float3 v) {
  return (float3)(v.x * c, v.y * c, v.z * c);
}

float3 reflection(const float3 normal, const float3 incidence) {
  return vectSub(incidence, vectConstMul(2, vectConstMul(dot(incidence, normal), normal)));
}

float vectLength(const float3 v) {
  return sqrt(sqr(v.x) + sqr(v.y) + sqr(v.z));
}

float3 vectNormalize(const float3 v) {
  const float len = vectLength(v);
  return (float3)(v.x / len, v.y / len, v.z / len);
}

float renderSphere(const float4 sphere, const float3 dir, const float3 lightDir, const float3 pos) {
  const float t = hit(pos, dir, sphere);
  if (t == -1.0)
    return 0;
  const float3 norm = vectNormalize(normal(pos, dir, t, sphere));
  const float3 reflect = reflection(norm, dir);
  const float angle = acos(dot(lightDir, vectNormalize(reflect)));
  return angle / 3.142;
}

int render(const float x, const float y) {
  const float3 pos = (float3)(x, y, 0.0);
  const float3 dir = (float3)(0.0, 0.0, 1.0);
  const float3 lightDir = vectNormalize((float3)(1.0, 1.0, 1.0));
  const float4 spheres[4] = {
    (float4)(0.5, 0.0, 50.0, 0.25),
    (float4)(-0.5, 0.0, 50.0, 0.25),
    (float4)(0.0, 0.3, 50.0, 0.25),
    (float4)(0.0, -0.3, 50.0, 0.25)
  };
  float s = 0.0;
  for (int i = 0; i < 4; ++i) {
    s += renderSphere(spheres[i], dir, lightDir, pos);
  }
  return (int) (s * 65535.0);
}

__kernel void sceneRender(const int width, const int height, const int workSize, __global int* output) {
  int workItem = get_global_id(0);
  float a[3] = {0.0f, 0.2f, 0.3f};
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    float x = pos % width - width / 2;
    float y = pos / width - height / 2;
    output[pos] = render(2.0 * x / width, 2.0 * y / width);
  }
}
