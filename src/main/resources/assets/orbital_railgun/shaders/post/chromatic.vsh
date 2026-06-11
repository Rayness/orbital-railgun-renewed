#version 330 compatibility

#moj_import <minecraft:projection.glsl>

in vec4 Position;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 DiffuseSize;
};

out vec2 texCoord;
out float viewHeight;
out float viewWidth;

void main() {
    vec4 outPos = ProjMat * vec4(Position.xy * OutSize, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    texCoord = Position.xy;
    viewHeight = OutSize.y;
    viewWidth = OutSize.x;
}
