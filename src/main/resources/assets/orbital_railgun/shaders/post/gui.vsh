#version 330

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 DiffuseSize;
    vec2 DepthSize;
};

out vec2 texCoord;
out float viewHeight;
out float viewWidth;

void main() {
    vec2 uv = vec2((gl_VertexID << 1) & 2, gl_VertexID & 2);
    gl_Position = vec4(uv * vec2(2.0, 2.0) + vec2(-1.0, -1.0), 0.0, 1.0);

    texCoord = uv;
    viewHeight = OutSize.y;
    viewWidth = OutSize.x;
}
