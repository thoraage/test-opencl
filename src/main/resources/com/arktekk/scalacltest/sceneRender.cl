struct vector {
  float x;
  float y;
  float z;
};

struct sphere {
  float x;
  float y;
  float z;
  float r;
};

struct spheres {
  struct sphere* ss;
  int c;
};

float sqr(const float v) {
  return v * v;
}

float hit(const struct vector pos, const struct vector dir, const struct sphere sphere) {
  const float a = sqr(dir.x) + sqr(dir.y) + sqr(dir.z);
  const float b = 2*dir.x*(pos.x - sphere.x) +  2*dir.y*(pos.y-sphere.y) +  2*dir.z*(pos.z-sphere.z);
  const float c = sqr(sphere.x) + sqr(sphere.y) + sqr(sphere.z) + sqr(pos.x) + sqr(pos.y) + sqr(pos.z) +
                                    -2*(sphere.x*pos.x + sphere.y*pos.y + sphere.z*pos.z) - sqr(sphere.r);
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

float vectDot(const struct vector v1, const struct vector v2) {
  return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
}

struct vector normal(const struct vector pos, const struct vector dir, const float t, const struct sphere sphere) {
  const struct vector hitPoint = { pos.x + t*dir.x, pos.y + t*dir.y, pos.z + t*dir.z };
  return (struct vector){hitPoint.x - sphere.x, hitPoint.y - sphere.y, hitPoint.z - sphere.z};
}

struct vector vectSub(const struct vector v1, const struct vector v2) {
  return (struct vector){v1.x - v2.x, v1.y - v2.y, v1.z - v2.z};
}

struct vector vectConstMul(const float c, const struct vector v) {
  return (struct vector){v.x * c, v.y * c, v.z * c};
}

struct vector reflection(const struct vector normal, const struct vector incidence) {
  return vectSub(incidence, vectConstMul(2, vectConstMul(vectDot(incidence, normal), normal)));
}

float vectLength(const struct vector v) {
  return sqrt(sqr(v.x) + sqr(v.y) + sqr(v.z));
}

struct vector vectNormalize(const struct vector v) {
  const float len = vectLength(v);
  return (struct vector) {v.x / len, v.y / len, v.z / len};
}

float trace(const struct spheres spheresStruct, const struct vector dir, const struct vector lightDir, const struct vector pos) {
  float s = 0.0;
  float t = -1.0;
  int idx = -1;
  for (int i = 0; i < spheresStruct.c; ++i) {
    const struct sphere sphere = spheresStruct.ss[i];
    const float tmp = hit(pos, dir, sphere);
    if (tmp != -1.0 && (t == -1.0 || tmp < t)) {
      t = tmp;
      idx = i;
    }
  }
  if (t == -1.0)
    return 0;
  const struct sphere sphere = spheresStruct.ss[idx];
  const struct vector norm = vectNormalize(normal(pos, dir, t, sphere));
  const struct vector reflect = reflection(norm, dir);
  const float angle = acos(vectDot(lightDir, vectNormalize(reflect)));
  s += angle / 3.142;
  return s;
}

int render(const float x, const float y) {
  const struct vector pos = {x, y, 0.0};
  const struct vector dir = {0.0, 0.0, 1.0};
  const struct vector lightDir = vectNormalize((struct vector){1.0, 1.0, 1.0});
  const struct sphere spheres[3] = {
    {0.5, 0.0, 5.0, 0.25},
    {-0.5, 0.0, 5.0, 0.25},
    {0.0, 0.0, 4.5, 0.25}
  };
  const struct spheres spheresStruct = { spheres, 3 };
  return (int) (trace(spheresStruct, dir, lightDir, pos) * 65535.0);
}

__kernel void sceneRender(const int width, const int height, const int workSize, __global int* output) {
  struct vector a = {0.0f, 0.2f, 0.3f};
  int workItem = get_global_id(0);
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    float x = pos % width - width / 2;
    float y = pos / width - height / 2;
    output[pos] = render(2.0 * x / width, 2.0 * y / width);
  }
}
