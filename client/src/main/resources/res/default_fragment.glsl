#version 150 core

in vec2 f_texPos;
in vec3 f_color;

out vec4 fragColor;

uniform sampler2D u_texture;

void main() {
    vec4 textureColor = texture(u_texture, f_texPos);
    fragColor = vec4(f_color, 1.0) * textureColor;
}
