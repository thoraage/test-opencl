
struct spheres {
  float4* ss;
  int c;
};

inline float hit(float4 pos, float4 dir, float4 sphere) {
  float sphereRadius = sphere.w;
  sphere.w = 0;
  float a = dot(dir, dir);
  float b = 2 * dot(dir, pos - sphere);
  float c = dot(sphere, sphere) + dot(pos, pos) +
                                    -2.0f * dot(sphere, pos) - sphereRadius * sphereRadius;
  float p1 = b * b - 4*a*c;
  if (p1 > 0) {
  	float s = sqrt(p1);
  	float d = 2*a;
    float t1 = (-b - s) / d;
    float t2 = (-b + s) / d; 
    if (t1 > 0 && t2 > 0)
      return (t1 < t2) ? t1 : t2;
    else
      return -1.0f;
  } else
    return -1.0f;
}


inline float4 normal(float4 pos, float4 dir, float t, float4 sphere) {
  float4 hitPoint = pos + t * dir;
  sphere.w = 0; // take radius off
  return fast_normalize(hitPoint - sphere);
}

inline float4 reflection(float4 normal, float4 incidence) {
  return incidence - normal * (2.0f * dot(incidence, normal));
}

float trace(struct spheres spheresStruct, float4 origDir, float4 lightDir, float4 origPos) {
  float s = 0.0f;
  float4 dir = origDir;
  float4 pos = origPos;
  for (int round = 0; round < 10; ++round) {
    float t = -1.0f;
    int idx = -1;
    for (int i = 0; i < spheresStruct.c; ++i) {
      float4 sphere = spheresStruct.ss[i];
      float tmp = hit(pos, dir, sphere);
      if (tmp != -1.0f && (t == -1.0f || tmp < t)) {
        t = tmp;
        idx = i;
      }
    }
    if (t == -1.0f)
      return s;
    float4 sphere = spheresStruct.ss[idx];
    float4 norm = normal(pos, dir, t, sphere);
    float4 reflect = reflection(norm, dir);
    float angle = acos(dot(lightDir, fast_normalize(reflect)));
    s += pow(0.25f, round) * angle / 3.142f;
    pos = t * dir + pos;
    dir = reflect;
  }
  return s;
}

int render(float x, float y, struct spheres spheresStruct, float4 dir, float4 lightDir) {
  float4 pos = {x, y, 0.0f, 0.0f};
  float s = trace(spheresStruct, dir, lightDir, pos);
  int value = (int)((s > 1.0f ? 1.0f : s) * 255.0f);
  return value + (value << 8) + (value << 16);
}

#if 0
// Enqueue in 2D : kernel.enqueueNDRange(queue, Array(width, height), Array(workGroupSize, workGroupSize))
__kernel void sceneRender(int width, int height, int workSize, __global int* output) {
  int x = get_global_id(0);
  int y = get_global_id(1);
  if (x >= width || y >= height)
  	return;
  
  int pos = y * width + x;
  float4 spheres[3] = {
    {0.5f, 0.0f, 5.0f, 0.25f},
    {-0.5f, 0.0f, 5.0f, 0.25f},
    {0.0f, 0.0f, 4.5f, 0.25f}
  };
  struct spheres spheresStruct = { spheres, 3 };
  float4 dir = {0.0f, 0.0f, 1.0f, 0.0f};
  float4 lightDir = fast_normalize((float4)(1, 1, 1, 0));
  float fx = x - width / 2;
  float fy = y - height / 2;
  output[pos] = render(2.0f * fx / width, 2.0f * fy / width, spheresStruct, dir, lightDir);
}
#else
// Enqueue in 1D
__kernel void sceneRender(int width, int height, int workSize, __global int* output) {
  int workItem = get_global_id(0);
  float4 spheres[3] = {
    {0.5f, 0.0f, 5.0f, 0.25f},
    {-0.5f, 0.0f, 5.0f, 0.25f},
    {0.0f, 0.0f, 4.5f, 0.25f}
  };
  struct spheres spheresStruct = { spheres, 3 };
  float4 dir = {0.0f, 0.0f, 1.0f, 0.0f};
  float4 lightDir = fast_normalize((float4)(1, 1, 1, 0));
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    float x = pos % width - width / 2;
    float y = pos / width - height / 2;
    output[pos] = render(2.0f * x / width, 2.0f * y / width, spheresStruct, dir, lightDir);
  }
}
#endif
