#version 120

in vec2 f_texPos;
in vec3 f_color;

uniform sampler2D texImage;

void main() {
    vec4 texColor = texture(texImage, f_texPos);
	gl_FragColor = vec4(f_color, 1.0) * texColor;
}
