#version 150

#moj_import <minecraft:matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec2 texCoord0;
in vec4 texProj0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
#endif

    if (color.x < 0.1) {
        // Move this calc to vertex
        vec2 coords = (texProj0.xy / texProj0.w) - 0.5;
        vec2 correctedCoords = clamp(vec2(coords.x, coords.y / texProj0.z) + 0.5, 0.0, 1.0);

        color = texture(Sampler1, correctedCoords);
    }

    fragColor = color;
}